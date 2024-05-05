package Dashboard_Table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashboardController extends MainController implements Initializable {

    @FXML
    private TableView<AttendanceInfo> tableView;

    @FXML
    private TableColumn<AttendanceInfo, Date> dateColumn;

    @FXML
    private TableColumn<AttendanceInfo, Time> checkInColumn;

    @FXML
    private TableColumn<AttendanceInfo, Time> checkOutColumn;

    @FXML
    private TableColumn<AttendanceInfo, Double> overtimeColumn;

    private int ID = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        overtimeColumn.setCellValueFactory(new PropertyValueFactory<>("overtime"));

        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/employee";
            String user = "...";
            String password = "...";
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                String sql = "SELECT id, name, date, check_in, check_out, overtime_hours FROM attendance_info WHERE ID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,ID);
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<AttendanceInfo> userInfoList = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Date date = resultSet.getDate("date");
                    Time checkIn = resultSet.getTime("check_in");
                    Time checkOut = resultSet.getTime("check_out");
                    double overtime = resultSet.getDouble("overtime_hours");

                    userInfoList.add(new AttendanceInfo(id, name, date, checkIn, checkOut, overtime));
                }

                tableView.setItems(userInfoList);

                resultSet.close();
                preparedStatement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
