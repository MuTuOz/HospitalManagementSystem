-- HOSPITAL MANAGEMENT SYSTEM - DML (Data Manipulation Language)
USE hospital_management_system;

-- Role Tablosu - Roller
INSERT INTO Role (name, description) VALUES
('Admin', 'Sistem yöneticisi, tüm yetkilere sahip'),
('Manager', 'Hastane yöneticisi, departman ve personel yönetimi'),
('Doctor', 'Doktor, hasta muayene ve tedavi yetkisi'),
('Patient', 'Hasta, randevu alma ve tıbbi kayıt görüntüleme');

-- User Tablosu - Kullanıcılar
INSERT INTO User (name, email, password, phone, address, role_id) VALUES
-- Adminler
('Mehmet Yılmaz', 'mehmet.yilmaz@gmail.com', '$2y$10$abcdefghijklmnopqrstuv', '0532 456 7890', 'Bağdat Cad. No:45 D:8 Kadıköy/İstanbul', 1),
('Ayşe Demir', 'ayse.demir@outlook.com', '$2y$10$bcdefghijklmnopqrstuv1', '0542 678 9012', 'Atatürk Bulvarı No:123 Çankaya/Ankara', 1),

-- Managerlar
('Zeynep Kaya', 'zeynep.kaya@gmail.com', '$2y$10$cdefghijklmnopqrstuv12', '0533 789 0123', 'İnönü Cad. No:67 Konak/İzmir', 2),
('Ali Şahin', 'ali.sahin@hotmail.com', '$2y$10$defghijklmnopqrstuv123', '0544 890 1234', 'Cumhuriyet Mah. 15. Sok. No:34 Nilüfer/Bursa', 2),
('Elif Öztürk', 'elif.ozturk@gmail.com', '$2y$10$efghijklmnopqrstuv1234', '0535 901 2345', 'Kızılay Mah. Ziya Gökalp Cad. No:89 Sıhhiye/Ankara', 2),

-- Doktorlar
('Dr. Ahmet Çelik', 'ahmet.celik@gmail.com', '$2y$10$fghijklmnopqrstuv12345', '0536 012 3456', 'Bostancı Mah. Mehmet Akif Cad. No:12 Kadıköy/İstanbul', 3),
('Dr. Fatma Yıldız', 'fatma.yildiz@outlook.com', '$2y$10$ghijklmnopqrstuv123456', '0545 123 4567', 'Alsancak Mah. Kıbrıs Şehitleri Cad. No:56 Konak/İzmir', 3),
('Dr. Mustafa Aydın', 'mustafa.aydin@gmail.com', '$2y$10$hijklmnopqrstuv1234567', '0537 234 5678', 'Yenişehir Mah. Atatürk Bulvarı No:78 Merkezefendi/Denizli', 3),
('Dr. Selin Arslan', 'selin.arslan@hotmail.com', '$2y$10$ijklmnopqrstuv12345678', '0546 345 6789', 'Çayyolu Mah. Alacaatlı Cad. No:45 Yenimahalle/Ankara', 3),
('Dr. Can Özdemir', 'can.ozdemir@gmail.com', '$2y$10$jklmnopqrstuv123456789', '0538 456 7890', 'Karşıyaka Mah. Bostanlı Cad. No:90 Karşıyaka/İzmir', 3),
('Dr. Deniz Aksoy', 'deniz.aksoy@outlook.com', '$2y$10$klmnopqrstuv1234567890', '0547 567 8901', 'Maltepe Mah. Bağdat Cad. No:234 Maltepe/İstanbul', 3),
('Dr. Emre Koç', 'emre.koc@gmail.com', '$2y$10$lmnopqrstuv12345678901', '0539 678 9012', 'Çankaya Mah. Tunalı Hilmi Cad. No:67 Çankaya/Ankara', 3),
('Dr. Gamze Türk', 'gamze.turk@hotmail.com', '$2y$10$mnopqrstuv123456789012', '0548 789 0123', 'Ümraniye Mah. Alemdağ Cad. No:123 Ümraniye/İstanbul', 3),
('Dr. Hakan Polat', 'hakan.polat@gmail.com', '$2y$10$nopqrstuv1234567890123', '0530 890 1234', 'Buca Mah. İzmir Cad. No:45 Buca/İzmir', 3),
('Dr. Işıl Erdoğan', 'isil.erdogan@outlook.com', '$2y$10$opqrstuv12345678901234', '0549 901 2345', 'Etiler Mah. Nispetiye Cad. No:78 Beşiktaş/İstanbul', 3),
('Dr. John Smith', 'john.smith@gmail.com', '$2y$10$pqrstuv123456789012345', '0531 012 3456', 'Konyaaltı Mah. Atatürk Blv. No:156 Konyaaltı/Antalya', 3),
('Dr. Maria Garcia', 'maria.garcia@outlook.com', '$2y$10$qrstuv1234567890123456', '0550 123 4567', 'Alsancak Mah. Gazi Bulvarı No:89 Konak/İzmir', 3),

-- Hastalar
('Kemal Taş', 'kemal.tas@gmail.com', '$2y$10$rstuv12345678901234567', '0532 234 5678', 'Esenler Mah. Davutpaşa Cad. No:23 Esenler/İstanbul', 4),
('Merve Çakır', 'merve.cakir@hotmail.com', '$2y$10$stuv123456789012345678', '0543 345 6789', 'Keçiören Mah. Etlik Cad. No:67 Keçiören/Ankara', 4),
('Burak Yalçın', 'burak.yalcin@gmail.com', '$2y$10$tuv1234567890123456789', '0534 456 7890', 'Kartal Mah. Ankara Cad. No:45 Kartal/İstanbul', 4),
('Nazlı Kara', 'nazli.kara@outlook.com', '$2y$10$uv12345678901234567890', '0544 567 8901', 'Bornova Mah. Kazım Dirik Cad. No:89 Bornova/İzmir', 4),
('Okan Tekin', 'okan.tekin@gmail.com', '$2y$10$v123456789012345678901', '0535 678 9012', 'Pendik Mah. Kaynarca Cad. No:123 Pendik/İstanbul', 4),
('Pınar Güneş', 'pinar.gunes@hotmail.com', '$2y$10$w234567890123456789012', '0545 789 0123', 'Nilüfer Mah. Fethiye Cad. No:34 Nilüfer/Bursa', 4),
('Serkan Ateş', 'serkan.ates@gmail.com', '$2y$10$x345678901234567890123', '0536 890 1234', 'Kadıköy Mah. Bahariye Cad. No:56 Kadıköy/İstanbul', 4),
('Tuğba Vural', 'tugba.vural@outlook.com', '$2y$10$y456789012345678901234', '0546 901 2345', 'Karşıyaka Mah. Cemal Gürsel Cad. No:78 Karşıyaka/İzmir', 4),
('Ufuk Özkan', 'ufuk.ozkan@gmail.com', '$2y$10$z567890123456789012345', '0537 012 3456', 'Beşiktaş Mah. Barbaros Blv. No:90 Beşiktaş/İstanbul', 4),
('Vildan Şen', 'vildan.sen@hotmail.com', '$2y$10$a678901234567890123456', '0547 123 4567', 'Çankaya Mah. Kızılay Mey. No:12 Çankaya/Ankara', 4),
('Yusuf Acar', 'yusuf.acar@gmail.com', '$2y$10$b789012345678901234567', '0538 234 5678', 'Maltepe Mah. Cevizli Cad. No:45 Maltepe/İstanbul', 4),
('Zehra Öz', 'zehra.oz@outlook.com', '$2y$10$c890123456789012345678', '0548 345 6789', 'Bayraklı Mah. Zübeyde Hanım Cad. No:67 Bayraklı/İzmir', 4),
('David Johnson', 'david.johnson@gmail.com', '$2y$10$d901234567890123456789', '0539 456 7890', 'Beşiktaş Mah. Ortaköy Cad. No:89 Beşiktaş/İstanbul', 4),
('Emily Wilson', 'emily.wilson@hotmail.com', '$2y$10$e012345678901234567890', '0549 567 8901', 'Alsancak Mah. Kordon Boyu No:123 Konak/İzmir', 4),
('Ahmed Hassan', 'ahmed.hassan@gmail.com', '$2y$10$f123456789012345678901', '0530 678 9012', 'Sultanahmet Mah. Divanyolu Cad. No:34 Fatih/İstanbul', 4),
('Aylin Demirtaş', 'aylin.demirtas@outlook.com', '$2y$10$g234567890123456789012', '0550 789 0123', 'Gaziemir Mah. Akçay Cad. No:56 Gaziemir/İzmir', 4),
('Barış Turan', 'baris.turan@gmail.com', '$2y$10$h345678901234567890123', '0531 890 1234', 'Şişli Mah. Halaskargazi Cad. No:78 Şişli/İstanbul', 4),
('Ceren Bulut', 'ceren.bulut@hotmail.com', '$2y$10$i456789012345678901234', '0541 901 2345', 'Çukurambar Mah. Dumlupınar Blv. No:90 Çankaya/Ankara', 4),
('Doruk Eren', 'doruk.eren@gmail.com', '$2y$10$j567890123456789012345', '0532 012 3456', 'Taksim Mah. İstiklal Cad. No:123 Beyoğlu/İstanbul', 4),
('Ebru Yurt', 'ebru.yurt@outlook.com', '$2y$10$k678901234567890123456', '0542 123 4567', 'Kartal Mah. Yakacık Cad. No:45 Kartal/İstanbul', 4);

-- Department Tablosu - Departmanlar
INSERT INTO Department (dept_name, location, phone) VALUES
('Kardiyoloji', 'A Blok 2. Kat', '0212 555 0101'),
('Ortopedi', 'B Blok 1. Kat', '0212 555 0102'),
('Nöroloji', 'A Blok 3. Kat', '0212 555 0103'),
('Pediatri', 'C Blok Zemin Kat', '0212 555 0104'),
('Göz Hastalıkları', 'B Blok 2. Kat', '0212 555 0105'),
('Dermatoloji', 'A Blok 1. Kat', '0212 555 0106'),
('Psikiyatri', 'D Blok 3. Kat', '0212 555 0107'),
('Üroloji', 'B Blok 3. Kat', '0212 555 0108');

-- Specialty Tablosu - Uzmanlıklar
INSERT INTO Specialty (name, description) VALUES
('Kardiyoloji', 'Kalp ve damar hastalıkları uzmanı'),
('Ortopedi ve Travmatoloji', 'Kemik, eklem ve kas hastalıkları uzmanı'),
('Nöroloji', 'Sinir sistemi hastalıkları uzmanı'),
('Pediatri', 'Çocuk hastalıkları uzmanı'),
('Göz Hastalıkları', 'Göz sağlığı ve hastalıkları uzmanı'),
('Dermatoloji', 'Cilt hastalıkları uzmanı'),
('Psikiyatri', 'Ruh sağlığı ve hastalıkları uzmanı'),
('Üroloji', 'İdrar yolları ve üreme sistemi hastalıkları uzmanı'),
('Dahiliye', 'İç hastalıkları uzmanı'),
('Genel Cerrahi', 'Genel cerrahi operasyonlar uzmanı');

-- Manager Tablosu - Yöneticiler
INSERT INTO Manager (user_id, position, hire_date, salary, qualification) VALUES
(3, 'Genel Müdür', '2018-03-15', 45000.00, 'İstanbul Üniversitesi İşletme Yüksek Lisans, 15 yıl sağlık yönetimi deneyimi'),
(4, 'İnsan Kaynakları Müdürü', '2019-06-01', 38000.00, 'Ankara Üniversitesi İnsan Kaynakları Bölümü, SHRM sertifikası'),
(5, 'Mali İşler Müdürü', '2020-01-10', 42000.00, 'Marmara Üniversitesi Maliye Bölümü, CPA sertifikası, 12 yıl deneyim');

-- Doctor Tablosu - Doktorlar
INSERT INTO Doctor (user_id, specialty_id, dept_id, license_no, experience, education, consultation_fee) VALUES
(6, 1, 1, 'DR123456', 15, 'İstanbul Tıp Fakültesi 2009, Kardiyoloji Uzmanlığı - Hacettepe Üniversitesi 2014', 800.00),
(7, 2, 2, 'DR123457', 12, 'Ege Üniversitesi Tıp Fakültesi 2012, Ortopedi Uzmanlığı - İstanbul Üniversitesi 2017', 750.00),
(8, 3, 3, 'DR123458', 10, 'Ankara Üniversitesi Tıp Fakültesi 2014, Nöroloji Uzmanlığı - Gazi Üniversitesi 2019', 700.00),
(9, 4, 4, 'DR123459', 8, 'Hacettepe Üniversitesi Tıp Fakültesi 2016, Pediatri Uzmanlığı - Ankara Üniversitesi 2021', 650.00),
(10, 5, 5, 'DR123460', 14, 'İzmir Dokuz Eylül Üniversitesi Tıp Fakültesi 2010, Göz Hastalıkları Uzmanlığı - İstanbul Üniversitesi 2015', 720.00),
(11, 6, 6, 'DR123461', 11, 'Marmara Üniversitesi Tıp Fakültesi 2013, Dermatoloji Uzmanlığı - Hacettepe Üniversitesi 2018', 680.00),
(12, 7, 7, 'DR123462', 9, 'Gazi Üniversitesi Tıp Fakültesi 2015, Psikiyatri Uzmanlığı - Ankara Üniversitesi 2020', 750.00),
(13, 8, 8, 'DR123463', 13, 'Çukurova Üniversitesi Tıp Fakültesi 2011, Üroloji Uzmanlığı - İstanbul Üniversitesi 2016', 700.00),
(14, 1, 1, 'DR123464', 7, 'Dokuz Eylül Üniversitesi Tıp Fakültesi 2017, Kardiyoloji Uzmanlığı - Ege Üniversitesi 2022', 650.00),
(15, 2, 2, 'DR123465', 18, 'Harvard Medical School 2006, Ortopedi Fellowship - Johns Hopkins 2011', 1200.00),
(16, 3, 3, 'DR123466', 16, 'University of Oxford Medical School 2008, Nöroloji Uzmanlığı - Cambridge 2013', 1100.00);

-- Patient Tablosu - Hastalar
INSERT INTO Patient (user_id, blood_type, date_of_birth, insurance_no, emergency_contact) VALUES
(17, 'A+', '1985-05-20', 'SGK-1234567890', 'Emel Taş - 0532 234 5679'),
(18, 'B+', '1992-08-15', 'SGK-1234567891', 'Can Çakır - 0543 345 6788'),
(19, '0+', '1988-03-10', 'SGK-1234567892', 'Ayşe Yalçın - 0534 456 7889'),
(20, 'AB+', '1995-11-25', 'SGK-1234567893', 'Mehmet Kara - 0544 567 8900'),
(21, 'A-', '1980-07-08', 'SGK-1234567894', 'Fatma Tekin - 0535 678 9011'),
(22, '0+', '1998-02-14', 'SGK-1234567895', 'Ali Güneş - 0545 789 0122'),
(23, 'B+', '1987-09-30', 'SGK-1234567896', 'Zeynep Ateş - 0536 890 1233'),
(24, 'A+', '1993-12-05', 'SGK-1234567897', 'Ahmet Vural - 0546 901 2344'),
(25, 'AB-', '1990-04-18', 'SGK-1234567898', 'Elif Özkan - 0537 012 3455'),
(26, '0-', '1986-06-22', 'SGK-1234567899', 'Mustafa Şen - 0547 123 4566'),
(27, 'A+', '1994-01-12', 'SGK-1234567900', 'Selin Acar - 0538 234 5677'),
(28, 'B-', '1991-10-28', 'SGK-1234567901', 'Can Öz - 0548 345 6788'),
(29, '0+', '1975-03-15', 'PVT-9876543210', 'Sarah Johnson - 0539 456 7899'),
(30, 'A+', '1982-07-20', 'PVT-9876543211', 'Michael Wilson - 0549 567 8900'),
(31, 'AB+', '1989-11-08', 'SGK-1234567902', 'Fatma Hassan - 0530 678 9011'),
(32, 'B+', '1996-05-30', 'SGK-1234567903', 'Mehmet Demirtaş - 0550 789 0122'),
(33, 'A-', '1984-09-12', 'SGK-1234567904', 'Ayşe Turan - 0531 890 1233'),
(34, '0+', '1997-02-25', 'SGK-1234567905', 'Ali Bulut - 0541 901 2344'),
(35, 'A+', '1983-08-17', 'SGK-1234567906', 'Zeynep Eren - 0532 012 3455'),
(36, 'B+', '1999-12-03', 'SGK-1234567907', 'Ahmet Yurt - 0542 123 4566');

-- Availability Tablosu - Doktor Müsaitlik Durumları (Ocak 2025 - Mart 2025)

-- Availability Tablosu - Doktor Müsaitlik Durumları (YENİDEN)
INSERT INTO Availability (doctor_id, date, time_slot, is_booked) VALUES
-- Dr. Ahmet Çelik (doctor_id: 1) - Ocak ayı program
(1, '2025-01-15', '09:00:00', TRUE),   -- Randevu 1
(1, '2025-01-15', '10:00:00', TRUE),   -- Randevu 2
(1, '2025-01-15', '11:00:00', FALSE),
(1, '2025-01-15', '13:00:00', FALSE),
(1, '2025-01-15', '14:00:00', TRUE),   -- Randevu 3

(1, '2025-01-16', '09:00:00', TRUE),   -- Randevu 4
(1, '2025-01-16', '10:00:00', FALSE),
(1, '2025-01-16', '11:00:00', FALSE),

-- Dr. Fatma Yıldız (doctor_id: 2) - Ocak ayı
(2, '2025-01-18', '09:00:00', FALSE),
(2, '2025-01-18', '10:00:00', TRUE),   -- Randevu 5
(2, '2025-01-18', '11:00:00', FALSE),
(2, '2025-01-18', '13:00:00', TRUE),   -- Randevu 6

-- Dr. Mustafa Aydın (doctor_id: 3) - Ocak ayı
(3, '2025-01-22', '09:00:00', TRUE),   -- Randevu 7
(3, '2025-01-22', '10:00:00', TRUE),   -- Randevu 8
(3, '2025-01-22', '11:00:00', TRUE),   -- Randevu 9

-- Dr. Selin Arslan (doctor_id: 4) - Pediatri
(4, '2025-01-17', '09:00:00', TRUE),   -- Randevu 10
(4, '2025-01-17', '10:00:00', TRUE),   -- Randevu 11
(4, '2025-01-17', '11:00:00', TRUE),   -- Randevu 12

-- Dr. Can Özdemir (doctor_id: 5) - Göz
(5, '2025-01-21', '10:00:00', TRUE),   -- Randevu 13
(5, '2025-01-21', '11:00:00', TRUE),   -- Randevu 14
(5, '2025-01-21', '13:00:00', TRUE),   -- Randevu 15

-- Dr. Deniz Aksoy (doctor_id: 6) - Dermatoloji
(6, '2025-01-23', '09:00:00', TRUE),   -- Randevu 16
(6, '2025-01-23', '10:00:00', TRUE),   -- Randevu 17
(6, '2025-01-23', '11:00:00', FALSE),

-- Dr. Emre Koç (doctor_id: 7) - Psikiyatri
(7, '2025-01-19', '09:00:00', TRUE),   -- Randevu 18
(7, '2025-01-19', '10:00:00', TRUE),   -- Randevu 19
(7, '2025-01-19', '11:00:00', FALSE),

-- Dr. Gamze Türk (doctor_id: 8) - Üroloji
(8, '2025-01-20', '09:00:00', FALSE),
(8, '2025-01-20', '10:00:00', TRUE),   -- Randevu 20
(8, '2025-01-20', '11:00:00', TRUE),   -- Randevu 21

-- ŞUBAT AYI
(1, '2025-02-10', '09:00:00', TRUE),   -- Randevu 22
(2, '2025-02-12', '10:00:00', TRUE),   -- Randevu 23
(3, '2025-02-15', '14:00:00', TRUE),   -- Randevu 24

-- MART AYI (planlanmış randevular)
(1, '2025-03-05', '09:00:00', TRUE),   -- Randevu 25
(2, '2025-03-06', '10:00:00', TRUE),   -- Randevu 26
(3, '2025-03-07', '11:00:00', TRUE);   -- Randevu 27

-- YENİ Appointment Tablosu - Randevular (availability_id ile birlikte)
INSERT INTO Appointment (doctor_id, patient_id, availability_id, status, notes, diagnosis, prescription) VALUES
-- Ocak 2025 - Tamamlanmış Randevular
(1, 1, 1, 'completed', 'Göğüs ağrısı şikayeti ile başvurdu', 'Hafif hipertansiyon, düzenli takip önerildi', 'Ramipril 5mg 1x1, Aspirin 100mg 1x1'),
(1, 5, 2, 'completed', 'Düzenli kontrol', 'Kan basıncı normal seviyelerde', 'Mevcut tedaviye devam'),
(1, 10, 5, 'completed', 'Kalp çarpıntısı', 'EKG normal, stres kaynaklı', 'Yaşam tarzı önerileri'),
(1, 18, 6, 'completed', 'Kontrol muayenesi', 'Durum stabil', 'Mevcut tedaviye devam'),

(2, 2, 10, 'completed', 'Sol diz ağrısı', 'Menisküs yırtığı şüphesi, MR önerildi', 'Naproksen 500mg 2x1, Fizik tedavi 10 seans'),
(2, 11, 13, 'completed', 'Bel ağrısı şikayeti', 'Lomber disk herniasyonu', 'Diklofenak 75mg 2x1, fizik tedavi'),

(3, 3, 14, 'completed', 'Baş ağrısı ve uyuşma', 'Migren tanısı konuldu', 'Rizatriptan 10mg gerektiğinde, Topiramat 25mg 1x1'),
(3, 12, 15, 'completed', 'Kolda güç kaybı', 'Sinir sıkışması tespit edildi', 'B12 vitamini, fizik tedavi önerildi'),
(3, 20, 16, 'completed', 'Unutkanlık şikayeti', 'Yaşa bağlı hafıza kaybı', 'Vitamin desteği, beyin egzersizleri'),

(4, 4, 17, 'completed', 'Çocukta yüksek ateş', 'Üst solunum yolu enfeksiyonu', 'Parasetamol şurup 3x1, bol sıvı tüketimi'),
(4, 13, 18, 'completed', 'Çocukta ishal', 'Viral gastroenterit', 'Probiyotik, bol sıvı'),
(4, 1, 19, 'completed', 'Çocukta grip belirtileri', 'İnfluenza A', 'Oseltamivir süspansiyon, parasetamol'),

(5, 6, 20, 'completed', 'Görme bulanıklığı', 'Miyopi artışı tespit edildi', 'Gözlük reçetesi güncellendi'),
(5, 14, 21, 'completed', 'Göz kızarıklığı', 'Konjonktivit', 'Tobramisin göz damlası 4x1'),

(6, 7, 22, 'completed', 'Cilt döküntüsü', 'Kontakt dermatit', 'Betametazon krem 2x1, Antihistaminik tablet 1x1'),
(6, 15, 23, 'completed', 'Akne problemi', 'Akne vulgaris', 'Adapalen krem, antibiyotik'),
(6, 2, 24, 'completed', 'Saç dökülmesi', 'Androgenetik alopesi', 'Minoksidil losyon, biotin takviyesi'),

(7, 8, 25, 'completed', 'Uyku problemi ve kaygı', 'Anksiyete bozukluğu', 'Sertralin 50mg 1x1, psikoterapi önerildi'),
(7, 16, 26, 'completed', 'Depresyon belirtileri', 'Majör depresif bozukluk', 'Essitalopram 10mg 1x1, psikoterapi'),
(7, 3, 27, 'completed', 'Panik atak', 'Panik bozukluk', 'Alprazolam 0.5mg gerektiğinde, terapi'),

(8, 9, 28, 'completed', 'İdrar yaparken yanma', 'İdrar yolu enfeksiyonu', 'Siprofloksasin 500mg 2x1, 7 gün'),
(8, 17, 29, 'completed', 'Böbrek taşı şüphesi', 'Böbrek taşı tespit edildi', 'Bol sıvı, ağrı kesici, üroloji kontrolü'),
(8, 4, 30, 'completed', 'Prostat kontrolü', 'Benign prostat hiperplazisi', 'Tamsulosin 0.4mg 1x1'),

-- Şubat 2025 - Tamamlanmış Randevular
(2, 19, 31, 'completed', 'Omuz ağrısı', 'Rotator kaf tendiniti', 'NSAID, fizik tedavi 15 seans'),

-- Mart 2025 - Planlanmış Randevular
(1, 5, 32, 'scheduled', 'Kontrol muayenesi', NULL, NULL),
(2, 6, 33, 'scheduled', 'MR sonuç değerlendirmesi', NULL, NULL),
(3, 7, 34, 'scheduled', 'Baş ağrısı takibi', NULL, NULL);

-- Review Tablosu - Hasta Değerlendirmeleri (appointment_id'leri güncellenmiş)
INSERT INTO Review (patient_id, doctor_id, appointment_id, rating, comment, review_date, is_anonymous) VALUES
(1, 1, 1, 5, 'Dr. Ahmet Bey çok ilgili ve detaylı muayene yaptı. Hastalığımı çok iyi açıkladı ve tedavi süreci hakkında bilgi verdi. Kesinlikle tavsiye ederim.', '2025-01-16', FALSE),
(2, 2, 5, 5, 'Harika bir doktor! Diz ağrım için çok kapsamlı bir değerlendirme yaptı. MR sonuçlarını çok net açıkladı.', '2025-01-19', FALSE),
(3, 3, 7, 4, 'Dr. Mustafa Bey deneyimli ve bilgili. Migren tedavim için iyi bir plan oluşturdu. Tek eksik randevu saatinde biraz gecikme oldu.', '2025-01-23', FALSE),
(4, 4, 10, 5, 'Çocuğuma çok iyi davrandı. Hem beni hem çocuğumu rahatlatacak şekilde muayene yaptı. Çok teşekkür ederim.', '2025-01-18', FALSE),
(5, 1, 2, 5, 'Düzenli kontrollerim için gidiyorum. Her seferinde dikkatli ve özenli. Mükemmel bir kardiyolog.', '2025-01-16', FALSE),
(6, 5, 13, 5, 'Göz muayenesi çok profesyonelce yapıldı. Dr. Can Bey hem teknik hem de hasta ilişkileri açısından mükemmel.', '2025-01-22', FALSE),
(7, 6, 16, 4, 'Cilt problemim için etkili tedavi önerdi. İyileşme süreci iyi gidiyor. Sadece randevu almak biraz zor.', '2025-01-24', FALSE),
(8, 7, 18, 5, 'Anksiyete sorunlarım için çok yardımcı oldu. Hem ilaç hem de terapi konusunda yönlendirmesi çok iyiydi.', '2025-01-20', TRUE);

-- Medical_Record Tablosu (appointment_id'leri güncellenmiş)
INSERT INTO Medical_Record (patient_id, doctor_id, appointment_id, record_date, test_results, medications, notes) VALUES
(1, 1, 1, '2025-01-15', 'EKG: Normal sinüs ritmi, Ekokardiyografi: EF %60, normal kardiyak yapı', 'Ramipril 5mg 1x1, Aspirin 100mg 1x1', 'Hastanın kan basıncı 140/90 mmHg. Düzenli takip önerildi.'),
(2, 2, 5, '2025-01-18', 'MR: Medial menisküs posterior horn yırtığı', 'Naproksen 500mg 2x1', 'Hastaya 10 seans fizik tedavi önerildi. Cerrahi gereksinim değerlendirilecek.'),
(3, 3, 7, '2025-01-22', 'EEG: Normal, Kranial MR: Normal', 'Rizatriptan 10mg gerektiğinde, Topiramat 25mg 1x1', 'Migren atakları için tetikleyici faktörler anlatıldı.'),
(4, 4, 10, '2025-01-17', 'Kan testleri: Lökosit 12.000, CRP: 24 mg/L', 'Parasetamol şurup 3x1', 'Üst solunum yolu enfeksiyonu tanısı. 3 gün sonra kontrol.');



-- Randevu bilgilerini availability ile birlikte görüntüleme
-- SELECT 
--    a.appointment_id,
--    u_d.name as doctor_name,
--    u_p.name as patient_name,
--    av.date,
--    av.time_slot,
--    a.status,
--    a.notes
-- FROM Appointment a
-- JOIN Availability av ON a.availability_id = av.availability_id
-- JOIN Doctor d ON a.doctor_id = d.doctor_id
-- JOIN User u_d ON d.user_id = u_d.user_id
-- JOIN Patient p ON a.patient_id = p.patient_id
-- JOIN User u_p ON p.user_id = u_p.user_id;