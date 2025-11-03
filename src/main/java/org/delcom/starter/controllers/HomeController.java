package org.delcom.starter.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
@RestController
public class HomeController {
    @GetMapping("/")
    public String hello() {
        return "Hay Abdullah, selamat datang di pengembangan aplikasi dengan Spring Boot!";
    }
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
    // ✅ Method 1: Informasi NIM
    @GetMapping("/informasiNim/{nim}")
    public String informasiNim(@PathVariable String nim) {
        HashMap<String, String> prodi = new HashMap<>();
        prodi.put("11S", "Sarjana Informatika");
        prodi.put("12S", "Sarjana Sistem Informasi");
        prodi.put("14S", "Sarjana Teknik Elektro");
        prodi.put("21S", "Sarjana Manajemen Rekayasa");
        prodi.put("22S", "Sarjana Teknik Metalurgi");
        prodi.put("31S", "Sarjana Teknik Bioproses");
        prodi.put("114", "Diploma 4 Teknologi Rekayasa Perangkat Lunak");
        prodi.put("113", "Diploma 3 Teknologi Informasi");
        prodi.put("133", "Diploma 3 Teknologi Komputer");
        String prefix = nim.substring(0, 3);
        String angkatan = "20" + nim.substring(3, 5);
        int urutInt = Integer.parseInt(nim.substring(nim.length() - 3));
        String urutan = String.valueOf(urutInt);
        StringBuilder result = new StringBuilder();
        result.append("<h3>Informasi NIM ").append(nim).append(":</h3>");
        result.append("<p>Program Studi: ").append(prodi.getOrDefault(prefix, "Tidak diketahui")).append("</p>");
        result.append("<p>Angkatan: ").append(angkatan).append("</p>");
        result.append("<p>Urutan: ").append(urutan).append("</p>");
        return result.toString();
    }
    // ✅ Method 2: perolehanNilai
  @GetMapping("/perolehanNilai/{strBase64}")
public String perolehanNilai(@PathVariable String strBase64) {
    try {
        // Decode base64 ke teks asli
        String decoded = new String(Base64.getDecoder().decode(strBase64)).trim();
        String[] lines = decoded.split("\\r?\\n");
        if (lines.length < 6) {
            return "<p>Input tidak valid. Minimal harus memiliki 6 baris bobot.</p>";
        }
        // ✅ Ambil bobot dari 6 baris pertama
        int bobotPA = Integer.parseInt(lines[0].trim());
        int bobotT = Integer.parseInt(lines[1].trim());
        int bobotK = Integer.parseInt(lines[2].trim());
        int bobotP = Integer.parseInt(lines[3].trim());
        int bobotUTS = Integer.parseInt(lines[4].trim());
        int bobotUAS = Integer.parseInt(lines[5].trim());
        // Variabel akumulasi nilai
        int totalPA = 0, maxPA = 0;
        int totalT = 0, maxT = 0;
        int totalK = 0, maxK = 0;
        int totalP = 0, maxP = 0;
        int totalUTS = 0, maxUTS = 0;
        int totalUAS = 0, maxUAS = 0;
        // ✅ Mulai membaca data nilai dari baris ke-7
        for (int i = 6; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            if (line.equals("---")) break;
            String[] parts = line.split("\\|");
            if (parts.length < 3) continue;
            String simbol = parts[0].trim();
            int maks = Integer.parseInt(parts[1].trim());
            int nilai = Integer.parseInt(parts[2].trim());

            switch (simbol) {
                case "PA": 
                    maxPA += maks; 
                    totalPA += nilai; 
                    break;
                case "T": 
                    maxT += maks; 
                    totalT += nilai; 
                    break;
                case "K": 
                    maxK += maks; 
                    totalK += nilai; 
                    break;
                case "P": 
                    maxP += maks; 
                    totalP += nilai; 
                    break;
                case "UTS": 
                    maxUTS += maks; 
                    totalUTS += nilai; 
                    break;
                case "UAS": 
                    maxUAS += maks; 
                    totalUAS += nilai; 
                    break;
                default: 
                    break; // abaikan simbol tidak dikenal
            }
        }
        // Hitung rata-rata dan pembulatan
        double rataPA = (maxPA == 0) ? 0 : (totalPA * 100.0 / maxPA);
        double rataT = (maxT == 0) ? 0 : (totalT * 100.0 / maxT);
        double rataK = (maxK == 0) ? 0 : (totalK * 100.0 / maxK);
        double rataP = (maxP == 0) ? 0 : (totalP * 100.0 / maxP);
        double rataUTS = (maxUTS == 0) ? 0 : (totalUTS * 100.0 / maxUTS);
        double rataUAS = (maxUAS == 0) ? 0 : (totalUAS * 100.0 / maxUAS);
        int bulatPA = (int)Math.floor(rataPA);
        int bulatT = (int)Math.floor(rataT);
        int bulatK = (int)Math.floor(rataK);
        int bulatP = (int)Math.floor(rataP);
        int bulatUTS = (int)Math.floor(rataUTS);
        int bulatUAS = (int)Math.floor(rataUAS);
        double nilaiPA = (bulatPA / 100.0) * bobotPA;
        double nilaiT = (bulatT / 100.0) * bobotT;
        double nilaiK = (bulatK / 100.0) * bobotK;
        double nilaiP = (bulatP / 100.0) * bobotP;
        double nilaiUTS = (bulatUTS / 100.0) * bobotUTS;
        double nilaiUAS = (bulatUAS / 100.0) * bobotUAS;
        double totalNilai = nilaiPA + nilaiT + nilaiK + nilaiP + nilaiUTS + nilaiUAS;
        // Tentukan grade
        String grade;
        if (totalNilai >= 79.5) grade = "A";
        else if (totalNilai >= 72) grade = "AB";
        else if (totalNilai >= 64.5) grade = "B";
        else if (totalNilai >= 57) grade = "BC";
        else if (totalNilai >= 49.5) grade = "C";
        else if (totalNilai >= 34) grade = "D";
        else grade = "E";

        StringBuilder result = new StringBuilder();
        result.append("<h3>Perolehan Nilai:</h3>");
        result.append("<pre>");
        result.append(String.format(">> Partisipatif: %d/100 (%.2f/%d)%n", bulatPA, nilaiPA, bobotPA));
        result.append(String.format(">> Tugas: %d/100 (%.2f/%d)%n", bulatT, nilaiT, bobotT));
        result.append(String.format(">> Kuis: %d/100 (%.2f/%d)%n", bulatK, nilaiK, bobotK));
        result.append(String.format(">> Proyek: %d/100 (%.2f/%d)%n", bulatP, nilaiP, bobotP));
        result.append(String.format(">> UTS: %d/100 (%.2f/%d)%n", bulatUTS, nilaiUTS, bobotUTS));
        result.append(String.format(">> UAS: %d/100 (%.2f/%d)%n", bulatUAS, nilaiUAS, bobotUAS));
        result.append(String.format(">> Nilai Akhir: %.2f%n", totalNilai));
        result.append(String.format(">> Grade: %s%n", grade));
        result.append("</pre>");
        return result.toString();
    } catch (Exception e) {
        // ✅ PERBAIKAN: Tag HTML yang benar
        return "<p style='color: red;'>Error processing data: " + e.getMessage() + "</p>";
    }
}
// ✅ Method 3: perbedaanL (Sudah Diperbaiki)
@GetMapping("/perbedaanL/{strBase64}")
public String perbedaanL(@PathVariable String strBase64) {
    try {
        // Decode base64 ke teks asli
        String decoded = new String(Base64.getDecoder().decode(strBase64)).trim();

        // --- PERBAIKAN DIMULAI DI SINI ---
        // Pengecekan input kosong harus dilakukan SEBELUM .split()
        // karena "".split() menghasilkan array {""} yang length-nya 1.
        if (decoded.isEmpty()) {
            return "<p>Input tidak valid. Data matriks kosong.</p>";
        }
        
        String[] lines = decoded.split("\\r?\\n");
        // Baris 'if (lines.length == 0)' yang lama sudah dihapus
        // --- PERBAIKAN SELESAI ---
        
        // Ambil ukuran matriks dari baris pertama
        int n = Integer.parseInt(lines[0].trim());
        
        // Validasi jumlah baris
        if (lines.length < n + 1) {
            return "<p>Input tidak valid. Jumlah baris tidak sesuai dengan ukuran matriks.</p>";
        }
        
        // Membuat matriks dengan array 2 dimensi
        int[][] matrix = new int[n][n];
        
        // Input elemen matriks dari baris kedua dan seterusnya
        for (int i = 0; i < n; i++) {
            String[] elements = lines[i + 1].trim().split("\\s+");
            if (elements.length < n) {
                return "<p>Input tidak valid. Jumlah elemen pada baris " + (i + 1) + " tidak sesuai.</p>";
            }
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
        }
        
        // Menghitung nilai L, kebalikan L, dan tengah
        int nilaiL = 0;
        int nilaiKebalikanL = 0;
        int nilaiTengah = 0;
        
        StringBuilder result = new StringBuilder();
        result.append("<h3>Perbedaan L dan Kebalikannya:</h3>");
        result.append("<pre>");
        
        // Jika matriks 1x1
        if (n == 1) {
            nilaiTengah = matrix[0][0];
            result.append("Nilai L: Tidak Ada\n");
            result.append("Nilai Kebalikan L: Tidak Ada\n");
            result.append("Nilai Tengah: ").append(nilaiTengah).append("\n");
            result.append("Perbedaan: Tidak Ada\n");
            result.append("Dominan: ").append(nilaiTengah).append("\n");
            result.append("</pre>");
            return result.toString();
        }
        
        // Jika matriks 2x2
        if (n == 2) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    nilaiTengah += matrix[i][j];
                }
            }
            result.append("Nilai L: Tidak Ada\n");
            result.append("Nilai Kebalikan L: Tidak Ada\n");
            result.append("Nilai Tengah: ").append(nilaiTengah).append("\n");
            result.append("Perbedaan: Tidak Ada\n");
            result.append("Dominan: ").append(nilaiTengah).append("\n");
            result.append("</pre>");
            return result.toString();
        }
        
        // Menghitung nilai L (Seluruh kolom pertama + baris terakhir kecuali elemen paling kanan dan kiri)
        for (int i = 0; i < n; i++) {
            nilaiL += matrix[i][0];
        }
        for (int j = 1; j < n - 1; j++) {
            nilaiL += matrix[n - 1][j];
        }
        
        // Menghitung nilai kebalikan L (Seluruh baris pertama kecuali elemen paling kiri + Seluruh kolom terakhir kecuali elemen paling atas)
        for (int j = 1; j < n; j++) {
            nilaiKebalikanL += matrix[0][j];
        }
        for (int i = 1; i < n; i++) {
            nilaiKebalikanL += matrix[i][n - 1];
        }
        
        // Menghitung nilai tengah (hanya untuk matriks n >= 3)
        if (n % 2 == 1) {
            nilaiTengah = matrix[n / 2][n / 2];
        } else {
            int a = n / 2 - 1;
            int b = n / 2;
            nilaiTengah = matrix[a][a] + matrix[a][b] + matrix[b][a] + matrix[b][b];
        }
        
        // Menghitung perbedaan
        int perbedaan = Math.abs(nilaiL - nilaiKebalikanL);
        
        // Menentukan dominan
        int dominan;
        if (nilaiL > nilaiKebalikanL) {
            dominan = nilaiL;
        } else if (nilaiKebalikanL > nilaiL) {
            dominan = nilaiKebalikanL;
        } else {
            dominan = nilaiTengah;
        }
        
        // Menampilkan Output hasil
        result.append("Nilai L: ").append(nilaiL).append("\n");
        result.append("Nilai Kebalikan L: ").append(nilaiKebalikanL).append("\n");
        result.append("Nilai Tengah: ").append(nilaiTengah).append("\n");
        result.append("Perbedaan: ").append(perbedaan).append("\n");
        result.append("Dominan: ").append(dominan).append("\n"); // (Saya perbaiki typo "DominAN" Anda)
        result.append("</pre>");
        
        return result.toString();
        
    } catch (Exception e) {
        return "<p style='color: red;'>Error processing data: " + e.getMessage() + "</p>";
    }
}
// ✅ Method 4: palingTer
@GetMapping("/palingTer/{strBase64}")
public String palingTer(@PathVariable String strBase64) {
    try {
        // Decode base64 ke teks asli
        String decoded = new String(Base64.getDecoder().decode(strBase64)).trim();
        String[] lines = decoded.split("\\r?\\n");
        
        if (decoded.isEmpty()) {
// Baris ini sekarang akan dieksekusi oleh testPalingTer_EmptyInput
 return "<p>Input tidak valid. Data nilai kosong.</p>"; 
 }

        HashMap<Integer, Integer> hashMapNilai = new HashMap<>();
        ArrayList<Integer> daftarNilai = new ArrayList<>();
        HashMap<Integer, Integer> hashMapCounter = new HashMap<>();
        HashMap<Integer, Integer> hashMapTotalTerendah = new HashMap<>();
        HashMap<Integer, Integer> hashMapCounterTerbanyak = new HashMap<>();
        
        // Membaca input sampai menemukan '---' atau akhir data
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.equals("---")) break;
            
            // Konversi input string ke integer
            int nilai = Integer.parseInt(line);
            int count = 1;
            
            // Jika nilai sudah ada, tambahkan frekuensinya
            if (hashMapNilai.containsKey(nilai)) {
                count = hashMapNilai.get(nilai) + 1;
            }
            
            // Masukkan ke HashMap dan ArrayList
            hashMapNilai.put(nilai, count);
            daftarNilai.add(nilai);
        }
        
        if (daftarNilai.isEmpty()) {
            return "<p>Input tidak valid. Tidak ada nilai yang dimasukkan.</p>";
        }
        
        // Ubah ArrayList ke array int agar mudah diproses
        int[] arrayNilai = daftarNilai.stream().mapToInt(Integer::intValue).toArray();
        
        // Inisialisasi nilai tertinggi & terendah dengan elemen pertama
        int nilaiTertinggi = arrayNilai[0];
        int nilaiTerendah = arrayNilai[0];
        
        // Cari nilai tertinggi & terendah
        for (int i = 1; i < arrayNilai.length; i++) {
            if (nilaiTertinggi < arrayNilai[i]) {
                nilaiTertinggi = arrayNilai[i];
            }
            if (nilaiTerendah > arrayNilai[i]) {
                nilaiTerendah = arrayNilai[i];
            }
        }
        
        // Cari jumlah terendah (nilai * frekuensi paling kecil)
        int nilaiJumlahTerendah = arrayNilai[0];
        int jumlahTerendah = nilaiJumlahTerendah;
        
        for (int i = 0; i < arrayNilai.length; i++) {
            if (hashMapTotalTerendah.containsKey(arrayNilai[i])) {
                int newTotal = hashMapTotalTerendah.get(arrayNilai[i]) + arrayNilai[i];
                hashMapTotalTerendah.put(arrayNilai[i], newTotal);
            } else {
                hashMapTotalTerendah.put(arrayNilai[i], arrayNilai[i]);
            }

            if (arrayNilai[i] == nilaiJumlahTerendah) {
                jumlahTerendah = hashMapTotalTerendah.get(nilaiJumlahTerendah);
            } else if (jumlahTerendah > hashMapTotalTerendah.get(arrayNilai[i])) {
                nilaiJumlahTerendah = arrayNilai[i];
                jumlahTerendah = hashMapTotalTerendah.get(arrayNilai[i]);
            }
        }

        // Cari nilai terbanyak (frekuensi kemunculan tertinggi)
        int nilaiTerbanyak = 0;
        int frekuensiTerbanyak = 0;

        for (int i = 0; i < arrayNilai.length; i++) {
            int frekuensiSaatIni = hashMapNilai.get(arrayNilai[i]);
            if (frekuensiSaatIni > frekuensiTerbanyak) {
                frekuensiTerbanyak = frekuensiSaatIni;
            }
        }

        // Menentukan nilai mana yang terbanyak muncul
        for (int i = 0; i < arrayNilai.length; i++) {
            int count = 1;
            if (hashMapCounterTerbanyak.containsKey(arrayNilai[i])) {
                count = hashMapCounterTerbanyak.get(arrayNilai[i]) + 1;
            }

            hashMapCounterTerbanyak.put(arrayNilai[i], count);

            int frekuensiSaatIni = hashMapCounterTerbanyak.get(arrayNilai[i]);
            if (frekuensiSaatIni == frekuensiTerbanyak) {
                nilaiTerbanyak = arrayNilai[i];
                break;
            }
        }
        
        // Cari nilai tersedikit (frekuensi terkecil)
        int nilaiTerdikit = arrayNilai[0];
        int frekuensiTerdikit = 1;
        hashMapCounter.put(nilaiTerdikit, 1);

        for (int i = 1; i < arrayNilai.length; i++) {
            int count = 1;
            if (hashMapCounter.containsKey(arrayNilai[i])) {
                count = hashMapCounter.get(arrayNilai[i]) + 1;
            }

            hashMapCounter.put(arrayNilai[i], count);
            
            if (arrayNilai[i] != nilaiTerdikit) {
                continue; 
            } else if (arrayNilai[i] == nilaiTerdikit) {
                for (int j = i + 1; j < arrayNilai.length; j++) {
                    if (hashMapCounter.containsKey(arrayNilai[j])) {
                        continue;
                    } else if (!hashMapCounter.containsKey(arrayNilai[j])) {
                        hashMapCounter.put(arrayNilai[j], 1);
                        nilaiTerdikit = arrayNilai[j];
                        frekuensiTerdikit = hashMapCounter.get(nilaiTerdikit);
                        i = j;
                        break;
                    }
                }
            }
        }
        
        // Cari jumlah tertinggi (nilai * frekuensi terbesar)
        int jumlahTertinggi = arrayNilai[0];    
        int nilaiJumlahTertinggi = 0;
        int frekuensiNilaiJumlahTertinggi = 0;
        
        if (!hashMapNilai.isEmpty()) {
            for (HashMap.Entry<Integer, Integer> entry : hashMapNilai.entrySet()) {
                int frekuensiSaatIni = entry.getValue();
                int angkaSaatIni = entry.getKey();
                int jumlah = frekuensiSaatIni * angkaSaatIni;
                if (jumlah >= jumlahTertinggi && angkaSaatIni > nilaiJumlahTertinggi) {
                    jumlahTertinggi = jumlah;
                    nilaiJumlahTertinggi = angkaSaatIni;
                    frekuensiNilaiJumlahTertinggi = frekuensiSaatIni;
                }
            }
        }

        // Output hasil perhitungan
        StringBuilder result = new StringBuilder();
        result.append("<h3>Paling Ter:</h3>");
        result.append("<pre>");
        result.append("Tertinggi: ").append(nilaiTertinggi).append("\n");
        result.append("Terendah: ").append(nilaiTerendah).append("\n");
        result.append("Terbanyak: ").append(nilaiTerbanyak).append(" (").append(frekuensiTerbanyak).append("x)\n");
        result.append("Tersedikit: ").append(nilaiTerdikit).append(" (").append(frekuensiTerdikit).append("x)\n");
        result.append("Jumlah Tertinggi: ").append(nilaiJumlahTertinggi).append(" * ").append(frekuensiNilaiJumlahTertinggi).append(" = ").append(jumlahTertinggi).append("\n");
        result.append("Jumlah Terendah: ").append(nilaiJumlahTerendah).append(" * ").append(hashMapNilai.get(nilaiJumlahTerendah)).append(" = ").append(jumlahTerendah).append("\n");
        result.append("</pre>");
        
        return result.toString();
        
    } catch (Exception e) {
        return "<p style='color: red;'>Error processing data: " + e.getMessage() + "</p>";
    }
}
}