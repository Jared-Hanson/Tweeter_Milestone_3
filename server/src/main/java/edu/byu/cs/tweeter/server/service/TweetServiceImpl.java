package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.service.TweetService_I;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class TweetServiceImpl implements TweetService_I {
    @Override
    public TweetResponse postTweet(TweetRequest request){
        if (!getAuthTokenDAO().checkToken(request.getAuthToken())) {
            return new TweetResponse(false, "Invalid Authorization");
        }
        TweetResponse result =  getStoryDAO().postTweet(request);

        if (result.isSuccess()) {
            Gson gson = new Gson();
            String messageBody = gson.toJson(request);
            String queueUrl = "https://sqs.us-west-2.amazonaws.com/223082554131/SimpleTestQueue";

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(messageBody)
                    .withDelaySeconds(0);

            AmazonSQS sqs = getSQS();
            sqs.sendMessage(send_msg_request);
        }

        return result;
    }

    /**
     * Returns an instance of {@link StoryDAO}. Allows mocking of the TweetDAO class
     * for testing purposes. All usages of TweetDAO should get their TweetDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }

    public AmazonSQS getSQS() {
        return AmazonSQSClientBuilder.defaultClient();
    }
}
