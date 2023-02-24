package Application.DAO;

import Application.Model.Flight;
import Application.Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'flight'.
 * It contains similar values as the Flight class:
 * flight_id, which is of type int,
 * departure_city, which is of type varchar(255),
 * arrival_city, which is of type varchar(255).
 */
public class FlightDAO {



    /**
     * TODO: Retrieve all flights from the flight table.
     *
     * You only need to change the sql String and set preparedStatement parameters.
     *
     * @return all flights.
     */
    public List<Flight> getAllFlights(){
        Connection connection = ConnectionUtil.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM flight";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Flight flight = new Flight(rs.getInt("flight_id"), rs.getString("departure_city"),
                        rs.getString("arrival_city"));
                flights.add(flight);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return flights;
    }

    /**
     * TODO: Retrieve a specific flight using its flight ID.
     *
     * You only need to change the sql String and set preparedStatement parameters.
     *
     * Remember that the format of a select where statement written as a Java String looks something like this:
     * String sql = "select * from TableName where ColumnName = ?";
     * The question marks will be filled in by the preparedStatement setString, setInt, etc methods. they follow
     * this format, where the first argument identifies the question mark to be filled (left to right, starting
     * from zero) and the second argument identifies the value to be used:
     * preparedStatement.setInt(1,int1);
     *
     * @param id a flight ID.
     */
    public Flight getFlightById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM flight WHERE flight_id = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);
            //write preparedStatement's setString and setInt methods here.

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Flight flight = new Flight(rs.getInt("flight_id"), rs.getString("departure_city"),
                        rs.getString("arrival_city"));
                return flight;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Add a flight record into the database which matches the values contained in the flight object.
     * You can use the getters already written in the Flight class to retrieve its values (getDeparture_city(),
     * getArrival_city()). The flight_id will be automatically generated by the SQL database, and JDBC will be able
     * to retrieve the generated ID automatically. That means that you when you insert the flight, you only need
     * to define the departure_city and arrival_city values (two columns total!)
     *
     * You only need to change the sql String and set preparedStatement parameters.
     *
     * Remember that the format of a insert PreparedStatement written as a string works something like this:
     * String sql = "insert into TableName (ColumnName1, ColumnName2) values (?, ?);";
     * The question marks will be filled in by the preparedStatement setString, setInt, etc methods. they follow
     * this format, where the first argument identifies the question mark to be filled (left to right, starting
     * from zero) and the second argument identifies the value to be used:
     * preparedStatement.setString(1,string1);
     * preparedStatement.setString(2,string2);
     *
     * @param flight an object modelling a Flight. the flight object does not contain a flight ID.
     */
    public Flight insertFlight(Flight flight){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here. When inserting, you only need to define the departure_city and arrival_city
            //values (two columns total!)
            String sql = "INSERT INTO flight (departure_city, arrival_city) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setString(1, flight.getDeparture_city());
            preparedStatement.setString(2,flight.getArrival_city());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_flight_id = (int) pkeyResultSet.getLong(1);
                return new Flight(generated_flight_id, flight.getDeparture_city(), flight.getArrival_city());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Update the flight identified by the flight id to the values contained in the flight object.
     *
     * You only need to change the sql String and set preparedStatement parameters.
     *
     * Remember that the format of an update PreparedStatement written as a Java String looks something like this:
     * String sql = "update TableName set ColumnName1=?, ColumnName2=? where ColumnName3 = ?;";
     * The question marks will be filled in by the preparedStatement setString, setInt, etc methods. they follow
     * this format, where the first argument identifies the question mark to be filled (left to right, starting
     * from zero) and the second argument identifies the value to be used:
     * preparedStatement.setString(1,string1);
     * preparedStatement.setString(2,string2);
     * preparedStatement.setInt(3,int1);
     *
     * @param id a flight ID.
     * @param flight a flight object. the flight object does not contain a flight ID.
     */
    public void updateFlight(int id, Flight flight){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "UPDATE flight SET departure_city = ?, arrival_city = ? WHERE flight_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write PreparedStatement setString and setInt methods here.

            preparedStatement.setInt(3, id);
            preparedStatement.setString(1, flight.getDeparture_city());
            preparedStatement.setString(2, flight.getArrival_city());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * TODO: Retrieve all flights following a particular flight path.
     *
     * you only need to change the sql string and set preparedStatement parameters.
     *
     * Remember that the format of a select where statement written as a Java String looks something like this:
     * "select * from TableName where ColumnName1 = ? and ColumnName2 = ?;";
     * The question marks will be filled in by the preparedStatement setString, setInt, etc methods. they follow
     * this format, where the first argument identifies the question mark to be filled (left to right, starting
     * from zero) and the second argument identifies the value to be used:
     * preparedStatement.setString(1,"column 1 value");
     * preparedStatement.setString(2,"column 2 value");
     *
     * @param departure_city the departing city.
     * @param arrival_city the arriving city.
     * @return all flights from departure_city to arrival_city.
     */
    public List<Flight> getAllFlightsFromCityToCity(String departure_city, String arrival_city){
        Connection connection = ConnectionUtil.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM flight WHERE departure_city = ? AND arrival_city = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write PreparedStatement setString and setInt methods here.
            preparedStatement.setString(1, departure_city);
            preparedStatement.setString(2, arrival_city);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Flight flight = new Flight(rs.getInt("flight_id"), rs.getString("departure_city"),
                        rs.getString("arrival_city"));
                flights.add(flight);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return flights;
    }
}
