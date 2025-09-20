package com.hotelmanagmentproject;
import java.sql.*;
import java.util.Scanner;


public class HotelReservationSystem {

    private  static final String url = "jdbc:mysql://localhost:3306/hotel_db";

    private static final String username = "root";

    private static final  String password = "8725307@Pawan";



    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {

        try {
            Class.forName("java.sql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error due to " + e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            while (true){
                System.out.println();
                System.out.println("Hotel Managment System");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get room number");
                System.out.println("4. Update room number");
                System.out.println("5. Delete reservation");
                System.out.println("0. Exit the service or checkout");
                System.out.print("Choose an option: ");
                int input = sc.nextInt();

                switch (input){
                    case 1 :
                        reservationRoom(con, sc);
                        break;
                    case 2:
                        viewReservation(con);
                        break;
                    case 3:
                        getRoomNumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                        break;
                    case 0 :
                        exit(sc);
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice try once again...");
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void reservationRoom(Connection con, Scanner sc) throws SQLException{
        System.out.print("Enter guest name : ");
        String guestName = sc.next();
        sc.nextLine();
        System.out.print("Enter room number : ");
        int roomNumber = sc.nextInt();
        System.out.print("Enter contact number : ");
        String contactNumber = sc.next();

        String query = "INSERT INTO reservations(guest_name, room_number, contact_number) " +
                "VALUES('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";



        try(Statement stmt = con.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            if (rowsAffected >0){
                System.out.println("Data insertion Successfully");
            }else {
                System.out.println("Data insertion failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void viewReservation(Connection con){

        String query = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date from reservations;";




        try(Statement stmt = con.createStatement()) {
            ResultSet rs =  stmt.executeQuery(query);
            System.out.println("Current Reservation");
            System.out.println("+------------------+------------------+------------------+------------------+------------------------+");
            System.out.println("   Reservation Id  |    Guest Name    |    Room Number   |   Contact Number |   Reservation Date    |");
            System.out.println("+------------------+------------------+------------------+------------------+------------------------+");

            while(rs.next()){
                int reservationId = rs.getInt("reservation_id");
                String guestName = rs.getString("guest_name");
                int roomNumber =  rs.getInt("room_number");
                String contactNumber = rs.getString("contact_number");
                String reservationDate = rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-16d | %-16s | % -16d | %-16s | %-16s |\n", reservationId, guestName, roomNumber, contactNumber, reservationDate);
                System.out.println("+------------------+------------------+------------------+------------------+------------------------+");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public static void getRoomNumber(Connection con, Scanner sc){
//        System.out.print("Please Enter your reservation id :");
//        int reservationId = sc.nextInt();
//        sc.nextLine();
//        System.out.println("Please Enter Name: ");
//        String guestName = sc.next();
//
//        String query = "SELECT room_number FROM reservations " +
//                "where reservation_id = " + reservationId +
//                "and guest_name = '" + guestName +"'";
        System.out.print("Please Enter your reservation id: ");
        int reservationId = sc.nextInt();
        sc.nextLine(); // consume leftover newline

        System.out.print("Please Enter Name: ");
        String guestName = sc.nextLine(); // allows spaces in names

        String query = "SELECT room_number FROM reservations " +
                "WHERE reservation_id =" + reservationId + " " +
                "AND guest_name ='"+guestName+"'";

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            if(resultSet.next()){
                int roomNumber = resultSet.getInt("room_number");
                System.out.printf("Room number for %d reservation number and %s name is : %d \n", reservationId, guestName, roomNumber);
            }else {
                System.out.println("Room number is not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void updateReservation(Connection con, Scanner sc){
        System.out.println("Enter Your Registration Id: ");
        int reservationId = sc.nextInt();
        sc.nextLine();

        if(!isreservationExist(con, reservationId)){
            System.out.println("Reservation not found for the given Id");
            return;
        }

        System.out.print("Enter Guest Name : ");
        String guestName = sc.nextLine();
        System.out.print("Enter Your Room Number : ");
        int roomNumber = sc.nextInt();

        String query = "UPDATE reservations " +" " +
                "SET guest_name = '"+ guestName +"', room_number = '"+ roomNumber +"'" + "WHERE reservation_id = '"+ reservationId +'"';

        try(Statement stmt = con.createStatement()) {
            int rowsUpdated = stmt.executeUpdate(query);

            if (rowsUpdated>0){
                System.out.println("Data inserted successfully");
            }else{
                System.out.println("Data insertion failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void deleteReservation(Connection con, Scanner sc){
        System.out.println("Enter your reservation_id");
        int reservation_id = sc.nextInt();

        String query =  "DELETE FROM reservations WHERE reservation_id = '" +reservation_id+"'";

        try {
            Statement smt = con.createStatement();
            int rowsAffected = smt.executeUpdate(query);

            if (rowsAffected>0){
                System.out.println("Data deletion successful");
            }else {
                System.out.println("Data deletion unsuccessful");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void exit(Scanner sc) throws InterruptedException {
        System.out.println("Do you want to checkout? (y/n)");
        char input = sc.next().charAt(0);
        System.out.print("Exiting System");
        int i = 5;

        while (i!=0){
            try {
                System.out.print(".");
                Thread.sleep(450);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i--;

        }

        if (input == 'y' || input == 'Y') {
            System.exit(0);
        }




    }

    private static boolean isreservationExist( Connection con, int reservationId){
        try{
            String query = "SELECT reservation_id FROM reservations where reservation_id = " + reservationId;

            try (Statement smt = con.createStatement()) {
                ResultSet resultSet = smt.executeQuery(query);

                return resultSet.next();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
