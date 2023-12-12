package org.server.service;

import org.server.connection.DatabaseConnection;
import org.server.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {
    public ServiceUser() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public ModelMessage register(ModelRegister data) {
        //  Check user exit
        ModelMessage message = new ModelMessage();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(CHECK_USER);
            preparedStatement.setString(1, data.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                message.setAction(false);
                message.setMessage("User Already Exit");
            } else {
                message.setAction(true);
            }
            resultSet.close();
            preparedStatement.close();
            if (message.isAction()) {
                //  Insert User Register
                con.setAutoCommit(false);
                preparedStatement = con.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, data.getUserName());
                preparedStatement.setString(2, data.getPassword());
                preparedStatement.execute();

                ResultSet keysSet = preparedStatement.getGeneratedKeys();
                if (keysSet.next()) {
                    int userId = keysSet.getInt(1);
                    resultSet.close();
                    preparedStatement.close();

                    preparedStatement = con.prepareStatement(INSERT_USER_ACCOUNT);
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setString(2, data.getUserName());
                    preparedStatement.execute();
                    preparedStatement.close();
                    con.commit();
                    con.setAutoCommit(true);
                    message.setAction(true);
                    message.setMessage("Ok");
                    message.setData(new ModelUserAccount(userId, data.getUserName(), "", "", true));
                }

            }
        } catch (SQLException e) {
            message.setAction(false);
            message.setMessage("Server Error");
            try {
                if (!con.getAutoCommit()) {
                    con.rollback();
                    con.setAutoCommit(true);
                }
            } catch (Exception e1) {

            }
        }
        return message;
    }
    public ModelMessage login(ModelLogin login) throws SQLException {
        ModelMessage message = new ModelMessage();
        ModelUserAccount modelUserAccount = null;
        PreparedStatement preparedStatement = con.prepareStatement(LOGIN);
        preparedStatement.setString(1 ,login.getUsername());
        preparedStatement.setString(2, login.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            int id = resultSet.getInt(1);
            String username = resultSet.getString(2);
            String gender = resultSet.getString(3);
            String image = resultSet.getString(4);
            modelUserAccount = new ModelUserAccount(id, username, gender, image, true);
            message.setAction(true);
            message.setMessage("Ok");
            message.setData(modelUserAccount);
        } else {
            message.setAction(false);
            message.setMessage("User does not exist");
        }
        resultSet.close();
        preparedStatement.close();

        return message;
    }
    public List<ModelUserAccount> getUser(int exitUser) {
        List<ModelUserAccount> userAccounts = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(SELECT_USER_ACCOUNT);
            preparedStatement.setInt(1, exitUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String gender = resultSet.getString(3);
                String image = resultSet.getString(4);
                userAccounts.add(new ModelUserAccount(id, username, gender, image, checkUserStatus(id)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return userAccounts;
    }
    private boolean checkUserStatus(int id) {
        List<ModelClient> clients = Service.getInstance(null).getClients();
        for (ModelClient client : clients) {
            if(client.getModelUserAccount().getId() == id) {
                return true;
            }
        }

        return false;
    }
    //  SQL
    private final String LOGIN = "select id, user_accounts.username, gender, imagestring from users join user_accounts using (id) where users.username = ? and users.password = ? and user_accounts.status = '1'";
    private final String SELECT_USER_ACCOUNT = "select id, username, gender, imagestring from user_accounts where user_accounts.status = '1' and id<>?";
    private final String INSERT_USER = "insert into users (username, password) values (?,?)";
    private final String INSERT_USER_ACCOUNT = "insert into user_accounts (id, username) values(?, ?)";
    private final String CHECK_USER = "select id from users where username = ? limit 1";
    //  Instance
    private final Connection con;
}
