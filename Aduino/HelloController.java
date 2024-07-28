package com.example.tcpclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {  // 초기화
    public Label label_temperature;
    public ProgressBar progressBar_temperature;
    public Label label_humidity;
    public ProgressBar progressBar_humidity;

    public Circle red_led;
    public Circle blue_led;
    public Circle green_led;

    public Button button_red_led;
    public Button button_blue_led;
    public Button button_green_led;

    private boolean state_of_button_red_led;
    private boolean state_of_button_blue_led;
    private boolean state_of_button_green_led;

    /**
     * Socket 클래스
     *
     * @param url
     * @param resourceBundle
     */
    private final Socket socket;

    public HelloController() {
        this.state_of_button_red_led = false;
        this.state_of_button_blue_led = false;
        this.state_of_button_green_led = false;
        this.socket = new Socket();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.socket.connect(new InetSocketAddress("192.168.0.2", 9999));
        } catch (IOException e) {
            System.out.printf("%s\r\n", e.getMessage());
            System.out.printf("%s\r\n", "서버 통신이 안됨");
        }
        this.received_data_from_server(new ActionEvent());
    }

    private double change_progressBar_value(double x, double in_min,
                                            double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    /**
     * Thread를 사용해서 계속 서버에서 오는 데이터를 실시간 화면에 출력
     *
     * @param actionEvent
     */
    private void received_data_from_server(ActionEvent actionEvent) {
        Thread thread_of_receving = new Thread(() -> {
            while (true) {
                try {
                    byte[] bytes_data = new byte[512];  // 왜냐하면 BUFSIZ : 512
                    final InputStream input_stream = this.socket.getInputStream();  // socket에서 데이터 들어오는 곳
                    final int read_byte_count = input_stream.read(bytes_data);
                    if (read_byte_count == -1) {
                        throw new IOException();
                    }
                    final String[] parsing_data = new String
                            (bytes_data, 0, read_byte_count).trim().split(",");
                    if (parsing_data.length != 2) continue;
                    final double temperature = Double.parseDouble(parsing_data[0]);
                    final double humidity = Double.parseDouble(parsing_data[1]);
                    System.out.printf("온도 %s\r\n", temperature);
                    System.out.printf("습도 %s\r\n", humidity);
                    final double changed_temperature_for_progressBar =
                            this.change_progressBar_value(temperature, 0.0, 40.0, 0.0, 1.0);
                    final double changed_humidity_for_progressBar =
                            this.change_progressBar_value(humidity, 0.0, 100.0, 0.0, 1.0);
                    /**
                     *  출력값을 ProgressBar에 넣어줘야 함
                     *  그러나 ProgressBar는 thread라서  thread -> thread로 접근하면 프로그램이 죽는다(GUI)가 thread에 unsafe.
                     */
                    Platform.runLater(() -> {
                        progressBar_temperature.setProgress(changed_temperature_for_progressBar);
                        progressBar_humidity.setProgress(changed_humidity_for_progressBar);
                        label_temperature.setText(parsing_data[0]);
                        label_humidity.setText(parsing_data[1]);
                    });
                } catch (IOException e) {
                    System.out.printf("%s\r\n", e.getMessage());
                    System.out.printf("%s\r\n", "입력 데이터 오류");
                    try {
                        this.socket.close();
                    } catch (IOException ex) {
                        System.out.printf("%s\r\n", e.getMessage());
                        System.out.printf("%s\r\n", "socket.close() 오류");
                    }

                }
            }
        });
        thread_of_receving.start();
    }

    public void ButtonOnClickedRedLED(ActionEvent actionEvent) {
        System.out.printf("%s\r\n", "RED_LED 버튼 클릭");
        if (this.socket.isConnected()) {
            this.state_of_button_red_led ^= true;
            if (this.state_of_button_red_led) {
                this.red_led.setVisible(true);
                this.red_led.setFill(Paint.valueOf("red"));
                try {
                    byte[] byte_data = ("RED_LED_ON\n").getBytes("UTF-8");
                    final var output_stream = this.socket.getOutputStream();
                    output_stream.write(byte_data);
                    output_stream.flush(); // write 버퍼를 비우세요
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.button_red_led.setText("RED_LED_ON");
            } else {
                this.red_led.setFill(Paint.valueOf("black"));
                try {
                    byte[] byte_data = ("RED_LED_OFF\n").getBytes("UTF-8");
                    try {
                        final OutputStream outputStream = this.socket.getOutputStream();
                        final OutputStream output_stream = this.socket.getOutputStream();
                        output_stream.write(byte_data);
                        output_stream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                this.button_red_led.setText("RED_LED_OFF");
            }
        }
    }

    public void ButtonOnClickedBlueLED(javafx.event.ActionEvent actionEvent) {
        System.out.printf("%s\r\n", "BLUE_LED 버튼 클릭");
        if (this.socket.isConnected()) {
            this.state_of_button_blue_led ^= true;
            if (this.state_of_button_blue_led) {
                this.blue_led.setVisible(true);
                this.blue_led.setFill(Paint.valueOf("blue"));
                try {
                    byte[] byte_data = ("BLUE_LED_ON\n").getBytes("UTF-8");
                    final var output_stream = this.socket.getOutputStream();
                    output_stream.write(byte_data);
                    output_stream.flush(); // write 버퍼를 비우세요
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.button_blue_led.setText("BLUE_LED_ON");
            } else {
                this.blue_led.setFill(Paint.valueOf("black"));
                try {
                    byte[] byte_data = ("BLUE_LED_OFF\n").getBytes("UTF-8");
                    try {
                        final OutputStream outputStream = this.socket.getOutputStream();
                        final OutputStream output_stream = this.socket.getOutputStream();
                        output_stream.write(byte_data);
                        output_stream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                this.button_blue_led.setText("BLUE_LED_OFF");
            }
        }
    }
    public void ButtonOnClickedGreenLED(javafx.event.ActionEvent actionEvent) {
        System.out.printf("%s\r\n", "GREEN_LED 버튼 클릭");
        if (this.socket.isConnected()) {
            this.state_of_button_green_led ^= true;
            if (this.state_of_button_green_led) {
                this.green_led.setVisible(true);
                this.green_led.setFill(Paint.valueOf("green"));
                try {
                    byte[] byte_data = ("GREEN_LED_ON\n").getBytes("UTF-8");
                    final var output_stream = this.socket.getOutputStream();
                    output_stream.write(byte_data);
                    output_stream.flush(); // write 버퍼를 비우세요
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.button_green_led.setText("GREEN_LED_ON");
            } else {
                this.green_led.setFill(Paint.valueOf("black"));
                try {
                    byte[] byte_data = ("GREEN_LED_OFF\n").getBytes("UTF-8");
                    try {
                        final OutputStream outputStream = this.socket.getOutputStream();
                        final OutputStream output_stream = this.socket.getOutputStream();
                        output_stream.write(byte_data);
                        output_stream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                this.button_blue_led.setText("GREEN_LED_OFF");
            }
        }
    }
}
