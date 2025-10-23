package com.djchan.lcfjdemo.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

// https://docs.langchain4j.dev/tutorials/tools/
@Component
public class MathTool {
    @Tool(name = "sum", value = "Sums 2 given numbers")
    double sum(double a, double b) {
        return a + b;
    }

    @Tool(name = "squareRoot", value = "Returns a square root of a given number")
    double squareRoot(double x) {
        return Math.sqrt(x);
    }
}
