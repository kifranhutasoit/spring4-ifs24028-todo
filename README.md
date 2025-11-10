# Spring v4.0 Proyek Starter

## Logs

### 04-11-2025

- Memperbarui kebutuhan paket

### 29-10-2025

- Melakukan inisialisasi proyek
- Menambahkan method `sayHello()` pada HomeController
= Menambahkan pengujian untuk method `sayHello()`


## Syntax

### Melakukan Instal Ulang Kebutuhan Paket

command: `mvn clean install`

#### Windows: elakukan build ulang proyek dan membuka hasil laporan
command with open jacoco: `mvn clean test; start target\site\jacoco\index.html`

#### Mac: melakukan build ulang proyek dan membuka hasil laporan
command with open jacoco: `mvn clean test && open target\site\jacoco\index.html`

#### Linux: melakukan build ulang proyek dan membuka hasil laporan
command with open jacoco: `mvn clean test && xdg-open target\site\jacoco\index.html`

### Menjalankan Aplikasi

Command: `mvn spring-boot:run`

URL: http://localhost:8080

### Menjalankan Test Covertage

pre-command: `mvn clean install`

command: `./mvnw test jacoco:report`

command-check: `./mvnw clean test jacoco:check`

<<<<<<< HEAD
## Purpose

Proyek ini dibuat untuk tujuan **Pendidikan**.

=======
test
>>>>>>> 678978baab68e13ebfffdbf3cc654a39d4b0b094

