package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class AuthTokenDAO {
    public boolean checkToken(AuthToken token) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("authToken");

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", "authToken");
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":authToken", token.getToken());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#a = :authToken")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);

        ItemCollection<QueryOutcome> items = null;
        //List<User> users = new ArrayList<>();
        boolean old = false;

        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item;
            while(iterator.hasNext()) {
                item = iterator.next();
                //check if token is older than 10 minutes
                if((Instant.now().toEpochMilli() - (Long)item.get("epoch_date")) > 600000) {
                    old = true;
                }
                break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return old;
    }

    public void addToken(AuthToken token) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("authToken");

        try {
            table.putItem(new Item().withPrimaryKey("authToken", token.getToken())
                    .withNumber( "epoch_date", Instant.now().toEpochMilli())
                    .withString("alias", token.getUser().getAlias()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeToken(AuthToken token) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("authToken");

        try {
            table.deleteItem("authToken", token.getToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LogoutResponse logout(LogoutRequest request) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("authToken");

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", "authToken");
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":authToken", request.getAuthToken().getToken());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#a = :authToken")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);

        ItemCollection<QueryOutcome> items = null;
        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item;
            while(iterator.hasNext()) {
                item = iterator.next();
                if(!item.get("alias").equals(request.getUser().getAlias())) {
                    return new LogoutResponse(false, "Couldn't log out " + request.getUser().getAlias());
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new LogoutResponse(false, "Couldn't access table");
        }

        return new LogoutResponse(true, request.getUser().getAlias() + " successfully logged out");
    }
}
