package com.medeiros.assemblyline.controller;

import com.medeiros.assemblyline.model.Task;
import com.medeiros.assemblyline.service.AssemblyLineAdjuster;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "AssemblyLine")
@RestController
@RequestMapping("/api/v1/assembly-lines")
public class AssemblyLineController {

    private final AssemblyLineAdjuster assemblyLineAdjuster;

    public AssemblyLineController(AssemblyLineAdjuster assemblyLineAdjuster) {
        this.assemblyLineAdjuster = assemblyLineAdjuster;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createJson(@RequestBody List<Task> tasks, @RequestParam(value = "lines", defaultValue = "1") Integer lines) {
        List<String> output = assemblyLineAdjuster.ajust(lines, tasks);
        return ResponseEntity.ok(output);
    }

    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "lines", defaultValue = "1") Integer lines) throws IOException {
        Resource responseFile = assemblyLineAdjuster.ajust(lines, file.getInputStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"output.txt\"").body(responseFile);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}
