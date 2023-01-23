/*
 * Copyright 2021-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.azure.di.httptriggerdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

@Component
public class MyAzureFunction {

    @FunctionName("ditest")
    public String execute(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {
    	System.out.println("123");
    	String connectionUrl = "jdbc:mysql://" + "localhost" + ":"
				+ "3306" + "/" + "world";
        try {
        	Connection connection = DriverManager.getConnection(connectionUrl, "root", "admin");
        	System.out.println(connection);
        	Statement statement = connection.createStatement();
        	System.out.println(statement);
            // Create and execute a SELECT SQL statement.
            String selectSql = "Select * from city";
            // insert the data
            ResultSet rs = statement.executeQuery(selectSql);
            System.out.println(rs);
            while(rs.next()){
                //Display values
            	System.out.println("in rs");
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", Name: " + rs.getString("name"));
             }
            rs.close();
            statement.close();
            connection.close();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
        	System.out.println("in sql ex");
        	e.printStackTrace();
        }
        //System.out.println(cityRepo.findAll());
        return "test";
        //return echo.andThen(uppercase).apply(request.getBody().get());
    }
}
