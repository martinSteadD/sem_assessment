package com.napier.sem; // Defines the package name for organising classes

// These Imports MongoDB client classes for database interaction
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document; // BSON document class used to represent MongoDB records

public class populationApp {

    public static void main(String[] args)
    {
        // Connect to MongoDB container named "mongo-dbserver"
        // Assumes MongoDB is running and accessible on default port 27017
        MongoClient mongoClient = new MongoClient("mongo-dbserver");

        // Access (or create) a database named "mydb"
        MongoDatabase database = mongoClient.getDatabase("mydb");

        // Access (or create) a collection named "test" within "mydb"
        MongoCollection<Document> collection = database.getCollection("test");

        // Create a new document with student details
        Document doc = new Document("name", "Kevin Sim") // Student name
                .append("class", "DevOps")               // Class name
                .append("year", "2024")                  // Academic year
                .append("result", new Document("CW", 95).append("EX", 85));
        // Nested document for results: CW (coursework) and EX (exam)

        // Insert the document into the "test" collection
        collection.insertOne(doc);

        // Retrieve the first document from the collection
        Document myDoc = collection.find().first();

        // Print the document as a JSON string to the console
        System.out.println(myDoc.toJson());
    }
}

