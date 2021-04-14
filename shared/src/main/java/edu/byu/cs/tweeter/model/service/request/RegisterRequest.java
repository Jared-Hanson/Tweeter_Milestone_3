package edu.byu.cs.tweeter.model.service.request;

/**
 * Contains all the information needed to make a register request.
 */
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private byte [] imageBytes;
    private String imageUrl;

    /**
     * Creates an instance.
     *
     * @param firstName the first name of the user to be registered.
     * @param lastName the last name of the user to be registered.
     * @param username the username of the user to be registered.
     * @param password the password of the user to be registered.
     * @param imageBytes the bytes of the profile image of the user to be registered.
     */
    public RegisterRequest(String firstName, String lastName, String username, String password, byte[] imageBytes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.imageBytes = imageBytes;
    }

//    public RegisterRequest(String firstName, String lastName, String username, String password, String imageUrl) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.password = password;
//        this.imageUrl = imageUrl;
//    }

    public RegisterRequest() { }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Returns the username of the user to be registered by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user to be registered by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the first name of the user to be registered by this request.
     *
     * @return the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user to be registered by this request.
     *
     * @return the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the bytes of the profile image of the user to be registered by this request.
     *
     * @return the bytes of the profile image.
     */
    public byte[] getImageBytes() {
        return imageBytes;
    }
}
