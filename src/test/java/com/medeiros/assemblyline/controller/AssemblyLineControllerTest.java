package com.medeiros.assemblyline.controller;

import com.google.common.io.Resources;
import com.medeiros.assemblyline.parser.TaskParserTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssemblyLineControllerTest {

    @LocalServerPort
    private int randomPort;

    @BeforeEach
    public void before() {
        RestAssured.port = randomPort;
    }

    @Test
    public void whenPostThenReturnOk() {
        List<String> output = RestAssured.given()
                .body(TaskParserTest.TASKS)
                .queryParam("lines", 2)
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString())
                .when()
                .post("/api/v1/assembly-lines")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        Assertions.assertThat(output)
                .hasSize(26)
                .contains("Assembly line 1 :",
                        "09:00 Cutting of steel sheets 60min",
                        "10:00 Safety sensor assembly 60min",
                        "11:00 Axis calibration 30min",
                        "11:30 Assembly line cooling - maintenance 5min",
                        "12:00 Lunch",
                        "13:00 Nitriding process 45min",
                        "13:45 Compliance check 30min",
                        "14:15 Torque converter subsystem calibration 60min",
                        "15:15 Setup of lock and control device 45min",
                        "16:00 Seal installation 45min",
                        "16:45 Ginástica laboral 10min",
                        "\n",
                        "Assembly line 2 :",
                        "09:00 Austenpera (Heat treatment) 30min",
                        "09:30 Tempering sub-zero (Heat treatment) 45min",
                        "10:15 Pieces washing 45min",
                        "11:00 Steel bearing assembly 45min",
                        "12:00 Lunch",
                        "13:00 Injection subsystem assembly 60min",
                        "14:00 Navigation subsystem assembly 60min",
                        "15:00 Left stabilizer bar alignment 30min",
                        "15:30 Right stabilizer bar alignment 30min",
                        "16:00 Application of decals 30min",
                        "16:30 Ginástica laboral 10min",
                        "\n");
    }

    @Test
    public void whenPostFileThenReturnOk() throws IOException {
        URL resource = Resources.getResource("input.txt");
        String output = RestAssured.given()
                .multiPart("file", "file", resource.openStream())
                .queryParam("lines", 2)
                .when()
                .post("/api/v1/assembly-lines/file")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .asString();
        Assertions.assertThat(output)
                .isEqualTo("Assembly line 1 :\n"
                        + "09:00 Cutting of steel sheets 60min\n"
                        + "10:00 Safety sensor assembly 60min\n"
                        + "11:00 Axis calibration 30min\n"
                        + "11:30 Assembly line cooling - maintenance 5min\n"
                        + "12:00 Lunch\n"
                        + "13:00 Nitriding process 45min\n"
                        + "13:45 Compliance check 30min\n"
                        + "14:15 Torque converter subsystem calibration 60min\n"
                        + "15:15 Setup of lock and control device 45min\n"
                        + "16:00 Seal installation 45min\n"
                        + "16:45 Ginástica laboral 10min\n"
                        + "\n"
                        + "\n"
                        + "Assembly line 2 :\n"
                        + "09:00 Austenpera (Heat treatment) 30min\n"
                        + "09:30 Tempering sub-zero (Heat treatment) 45min\n"
                        + "10:15 Pieces washing 45min\n"
                        + "11:00 Steel bearing assembly 45min\n"
                        + "12:00 Lunch\n"
                        + "13:00 Injection subsystem assembly 60min\n"
                        + "14:00 Navigation subsystem assembly 60min\n"
                        + "15:00 Left stabilizer bar alignment 30min\n"
                        + "15:30 Right stabilizer bar alignment 30min\n"
                        + "16:00 Application of decals 30min\n"
                        + "16:30 Ginástica laboral 10min\n"
                        + "\n"
                        + "\n");
    }
        URL resource = Resources.getResource("input_full.txt");

    @Test
    public void whenPostBurstSizeThenReturnError() {
        RestAssured.given()
                .body(TaskParserTest.TASKS_FULL)
                .queryParam("lines", 2)
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString())
                .when()
                .post("/api/v1/assembly-lines/")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void whenPosFiletBurstSizeThenReturnError() throws IOException {
        URL resource = Resources.getResource("input_full.txt");
        RestAssured.given()
                .multiPart("file", "file", resource.openStream())
                .queryParam("lines", 2)
                .header("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post("/api/v1/assembly-lines/file")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}