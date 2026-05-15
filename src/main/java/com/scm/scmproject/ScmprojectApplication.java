package com.scm.scmproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScmprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScmprojectApplication.class, args);
	}
}
//npx @tailwindcss/cli -i ./src/main/resources/static/input.css -o ./src/main/resources/static/output.css --watch

//if the 8080 port is in another work and failed to start the application
/*
C:\Windows\System32>netstat -ano | findstr :8080
  TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       4792

C:\Windows\System32>taskkill /PID 4792 /F

/*
@theme {
    /* Default (light) background */
/* --color-page-bg: white;
}
/*
@layer theme {
    :root {

        /* Override for dark mode */
/*    @variant dark {
            --color-page-bg: #0f172a;
            /* Your custom dark color (e.g., slate-900) */
/*     }
    }
}*/