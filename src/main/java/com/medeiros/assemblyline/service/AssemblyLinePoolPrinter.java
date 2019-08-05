package com.medeiros.assemblyline.service;

import com.medeiros.assemblyline.model.AssemblyLine;
import com.medeiros.assemblyline.model.AssemblyLinePool;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AssemblyLinePoolPrinter {

    public List<String> print(AssemblyLinePool pool) {
        List<String> output = new ArrayList<>();
        List<AssemblyLine> lines = pool.getPool();
        for (int i = 0; i < lines.size(); i++) {
            output.add("Assembly line " + (i + 1) + " :");
            AssemblyLine line = lines.get(i);
            List<String> lineOutput = line.print();
            output.addAll(lineOutput);
            output.add("\n");
        }
        return output;
    }

}
