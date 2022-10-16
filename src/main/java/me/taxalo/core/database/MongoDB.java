package me.taxalo.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import me.taxalo.core.Core;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MongoDB {

    final Core plugin = Core.getInstance();
    final MongoClient mongoClient;

    @Getter
    private final MongoDatabase database;
    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> ranksCollection;

    public MongoDB(String mongoURI) {
        mongoClient = new MongoClient(new MongoClientURI(mongoURI));
        database = mongoClient.getDatabase("taxalocore");
        userCollection = database.getCollection("users");
        ranksCollection = database.getCollection("ranks");
    }

    public ArrayList<Document> getRanks() {
        return ranksCollection.find().into(new ArrayList<>());
    }

    public void createUser(UUID uuid, String color, ArrayList<String> ranks) {
        final Date date = new Date(System.currentTimeMillis());
        final Document user = new Document("_id", new ObjectId())
                .append("uuid", uuid)
                .append("color", color != null ? color : ChatColor.GRAY.toString())
                .append("ranks", ranks != null ? ranks : Collections.emptyList())
                .append("created", date);

        userCollection.insertOne(user);
    }

    public void setColor(UUID uuid, String color) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Document findUser = userCollection.find(Filters.eq("uuid", uuid)).first();

            if (findUser == null) {
                createUser(uuid, color, null);
                return;
            }

            userCollection.updateOne(Filters.eq("uuid", uuid), Updates.set("color", color));
        });
    }

    public void setUserRanks(UUID uuid, ArrayList<String> ranks) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Document findUser = userCollection.find(Filters.eq("uuid", uuid)).first();

            if (findUser == null) {
                createUser(uuid, null, ranks);
            }

            userCollection.updateOne(Filters.eq("uuid", uuid), Updates.set("ranks", ranks));
        });
    }

    public void addRank(String name, ArrayList<String> permissions) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Document findRank = ranksCollection.find(Filters.eq("name", name)).first();

            if (findRank != null) return;

            final Date date = new Date(System.currentTimeMillis());

            final Document rank = new Document("_id", new ObjectId())
                    .append("name", name)
                    .append("permissions", permissions == null ? Collections.emptyList() : permissions)
                    .append("created", date);

            ranksCollection.insertOne(rank);

        });
    }

    public void removeRank(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Document findRank = ranksCollection.find(Filters.eq("name", name)).first();

            if (findRank == null) return;

            ranksCollection.deleteOne(Filters.eq("name", name));

        });
    }

    public void addPermission(String rankName, String permission) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            final Document findRank = ranksCollection.find(Filters.eq("name", rankName)).first();

            if (findRank == null) return;

            final ArrayList<String> permissions = (ArrayList<String>) findRank.get("permissions");

            if (permission.length() == 0) {
                ranksCollection.updateOne(Filters.eq("name", rankName), Updates.set("permissions", (Collections.singleton(permission))));
            } else {
                permissions.add(permission);
                ranksCollection.updateOne(Filters.eq("name", rankName), Updates.set("permissions", permissions));
            }
        });
    }

    public void removePermission(String rankName, String permission) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            final Document findRank = ranksCollection.find(Filters.eq("name", rankName)).first();

            if (findRank == null) return;

            final ArrayList<String> permissions = (ArrayList<String>) findRank.get("permissions");

            if (permission.length() > 0) {
                permissions.remove(permission);
                ranksCollection.updateOne(Filters.eq("name", rankName), Updates.set("permissions", permissions));
            }
        });
    }

    public Document getUser(UUID uuid) {

        /*
         * Hay que usar CompletableFuture en vez del propio Scheduler de Bukkit
         * Porque necesitamos hacer una query a mongo en un segundo thread
         * Y devolver este resultado para poder ejecutarlo en el primer thread
         * para la API de NametagEdit
         */

        final CompletableFuture<Document> complete = CompletableFuture.supplyAsync(() -> userCollection.find(Filters.eq("uuid", uuid)).first());

        try {
            return complete.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
