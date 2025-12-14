-- HOSPITAL MANAGEMENT SYSTEM - DML (Güncellenmiş)
-- 5 İl, Her İlde 5 Hastane, Her Hastanede 7 Doktor
-- Her Doktorun 7 Availability ve 7 Appointment

USE hospital_management_system;

-- -----------------------------------------------------
-- 1. ROLLER
-- -----------------------------------------------------
INSERT INTO Role (name, description) VALUES
                                         ('Admin', 'Sistem yöneticisi, tüm yetkilere sahip'),
                                         ('Manager', 'Hastane yöneticisi, departman ve personel yönetimi'),
                                         ('Doctor', 'Doktor, hasta muayene ve tedavi yetkisi'),
                                         ('Patient', 'Hasta, randevu alma ve tıbbi kayıt görüntüleme');

-- -----------------------------------------------------
-- 2. KULLANICILAR (Adminler + Managerlar + 175 Doktor + 175 Hasta = 375+ User)
-- -----------------------------------------------------
INSERT INTO User (name, email, password, phone, address, role_id) VALUES
-- Adminler (user_id: 1-3)
('Mehmet Yılmaz', 'mehmet.yilmaz@admin.com', '$2y$10$abcdefgh', '05324567890', 'Kadıköy/İstanbul', 1),
('Ayşe Demir', 'ayse.demir@admin.com', '$2y$10$bcdefghi', '05426789012', 'Çankaya/Ankara', 1),
-- Test Admin (Plain text password for development)
('Test Admin', 'test@admin.com', 'admin123', '05551234567', 'Test Mahallesi/İstanbul', 1),

-- Managerlar (user_id: 4-28, Her hastane için 1 manager)
-- İstanbul Hastaneleri Managerları
('Zeynep Kaya', 'zeynep.kaya@manager.com', '$2y$10$cdefghij', '05337890123', 'Kadıköy/İstanbul', 2),
('Ali Şahin', 'ali.sahin@manager.com', '$2y$10$defghijk', '05448901234', 'Beşiktaş/İstanbul', 2),
('Elif Öztürk', 'elif.ozturk@manager.com', '$2y$10$efghijkl', '05359012345', 'Üsküdar/İstanbul', 2),
('Burak Yılmaz', 'burak.yilmaz@manager.com', '$2y$10$fghijklm', '05460123456', 'Şişli/İstanbul', 2),
('Merve Arslan', 'merve.arslan@manager.com', '$2y$10$ghijklmn', '05361234567', 'Bakırköy/İstanbul', 2),

-- Ankara Hastaneleri Managerları
('Can Demir', 'can.demir@manager.com', '$2y$10$hijklmno', '05372345678', 'Çankaya/Ankara', 2),
('Seda Yıldız', 'seda.yildiz@manager.com', '$2y$10$ijklmnop', '05483456789', 'Keçiören/Ankara', 2),
('Emre Korkmaz', 'emre.korkmaz@manager.com', '$2y$10$jklmnopq', '05394567890', 'Yenimahalle/Ankara', 2),
('Deniz Aydın', 'deniz.aydin@manager.com', '$2y$10$klmnopqr', '05405678901', 'Mamak/Ankara', 2),
('Pınar Çelik', 'pinar.celik@manager.com', '$2y$10$lmnopqrs', '05316789012', 'Etimesgut/Ankara', 2),

-- İzmir Hastaneleri Managerları
('Okan Tekin', 'okan.tekin@manager.com', '$2y$10$mnopqrst', '05427890123', 'Konak/İzmir', 2),
('Selin Akgül', 'selin.akgul@manager.com', '$2y$10$nopqrstu', '05338901234', 'Karşıyaka/İzmir', 2),
('Tolga Şen', 'tolga.sen@manager.com', '$2y$10$opqrstuv', '05449012345', 'Bornova/İzmir', 2),
('Burcu Erdem', 'burcu.erdem@manager.com', '$2y$10$pqrstuvw', '05350123456', 'Buca/İzmir', 2),
('Volkan Güler', 'volkan.guler@manager.com', '$2y$10$qrstuvwx', '05461234567', 'Alsancak/İzmir', 2),

-- Bursa Hastaneleri Managerları
('Gamze Sarı', 'gamze.sari@manager.com', '$2y$10$rstuvwxy', '05372345678', 'Osmangazi/Bursa', 2),
('Hakan Koç', 'hakan.koc@manager.com', '$2y$10$stuvwxyz', '05483456789', 'Nilüfer/Bursa', 2),
('İpek Yılmaz', 'ipek.yilmaz@manager.com', '$2y$10$tuvwxyza', '05394567890', 'Yıldırım/Bursa', 2),
('Kaan Demir', 'kaan.demir@manager.com', '$2y$10$uvwxyzab', '05405678901', 'Gemlik/Bursa', 2),
('Lale Çetin', 'lale.cetin@manager.com', '$2y$10$vwxyzabc', '05316789012', 'İnegöl/Bursa', 2),

-- Antalya Hastaneleri Managerları
('Murat Aslan', 'murat.aslan@manager.com', '$2y$10$wxyzabcd', '05427890123', 'Muratpaşa/Antalya', 2),
('Nazan Kaya', 'nazan.kaya@manager.com', '$2y$10$xyzabcde', '05338901234', 'Konyaaltı/Antalya', 2),
('Orhan Yıldırım', 'orhan.yildirim@manager.com', '$2y$10$yzabcdef', '05449012345', 'Kepez/Antalya', 2),
('Perihan Aktaş', 'perihan.aktas@manager.com', '$2y$10$zabcdefg', '05350123456', 'Lara/Antalya', 2),
('Rıza Özdemir', 'riza.ozdemir@manager.com', '$2y$10$abcdefgh', '05461234567', 'Alanya/Antalya', 2),

-- Doktorlar (user_id: 28-202, 7 doktor x 25 hastane = 175 doktor)
-- İstanbul 1. Hastane Doktorları (28-34)
('Dr. Ahmet Yılmaz', 'ahmet.yilmaz@doctor.com', '$2y$10$pass1', '05321000001', 'Kadıköy/İstanbul', 3),
('Dr. Ayşe Demir', 'ayse.demir@doctor.com', '$2y$10$pass2', '05321000002', 'Beşiktaş/İstanbul', 3),
('Dr. Mehmet Kaya', 'mehmet.kaya@doctor.com', '$2y$10$pass3', '05321000003', 'Üsküdar/İstanbul', 3),
('Dr. Fatma Şahin', 'fatma.sahin@doctor.com', '$2y$10$pass4', '05321000004', 'Şişli/İstanbul', 3),
('Dr. Mustafa Arslan', 'mustafa.arslan@doctor.com', '$2y$10$pass5', '05321000005', 'Bakırköy/İstanbul', 3),
('Dr. Zeynep Öztürk', 'zeynep.ozturk@doctor.com', '$2y$10$pass6', '05321000006', 'Fatih/İstanbul', 3),
('Dr. Emre Çelik', 'emre.celik@doctor.com', '$2y$10$pass7', '05321000007', 'Maltepe/İstanbul', 3),

-- İstanbul 2. Hastane Doktorları (35-41)
('Dr. Ali Yıldız', 'ali.yildiz@doctor.com', '$2y$10$pass8', '05321000008', 'Kadıköy/İstanbul', 3),
('Dr. Merve Korkmaz', 'merve.korkmaz@doctor.com', '$2y$10$pass9', '05321000009', 'Beşiktaş/İstanbul', 3),
('Dr. Can Aydın', 'can.aydin@doctor.com', '$2y$10$pass10', '05321000010', 'Üsküdar/İstanbul', 3),
('Dr. Seda Demirtaş', 'seda.demirtas@doctor.com', '$2y$10$pass11', '05321000011', 'Şişli/İstanbul', 3),
('Dr. Burak Şen', 'burak.sen@doctor.com', '$2y$10$pass12', '05321000012', 'Bakırköy/İstanbul', 3),
('Dr. Deniz Yılmaz', 'deniz.yilmaz@doctor.com', '$2y$10$pass13', '05321000013', 'Fatih/İstanbul', 3),
('Dr. Elif Arslan', 'elif.arslan@doctor.com', '$2y$10$pass14', '05321000014', 'Maltepe/İstanbul', 3),

-- İstanbul 3. Hastane Doktorları (42-48)
('Dr. Okan Tekin', 'okan.tekin@doctor.com', '$2y$10$pass15', '05321000015', 'Kadıköy/İstanbul', 3),
('Dr. Pınar Akgül', 'pinar.akgul@doctor.com', '$2y$10$pass16', '05321000016', 'Beşiktaş/İstanbul', 3),
('Dr. Serkan Şen', 'serkan.sen@doctor.com', '$2y$10$pass17', '05321000017', 'Üsküdar/İstanbul', 3),
('Dr. Tuğba Erdem', 'tugba.erdem@doctor.com', '$2y$10$pass18', '05321000018', 'Şişli/İstanbul', 3),
('Dr. Ufuk Güler', 'ufuk.guler@doctor.com', '$2y$10$pass19', '05321000019', 'Bakırköy/İstanbul', 3),
('Dr. Vildan Sarı', 'vildan.sari@doctor.com', '$2y$10$pass20', '05321000020', 'Fatih/İstanbul', 3),
('Dr. Yusuf Koç', 'yusuf.koc@doctor.com', '$2y$10$pass21', '05321000021', 'Maltepe/İstanbul', 3),

-- İstanbul 4. Hastane Doktorları (49-55)
('Dr. Zeliha Yılmaz', 'zeliha.yilmaz@doctor.com', '$2y$10$pass22', '05321000022', 'Kadıköy/İstanbul', 3),
('Dr. Ahmet Can', 'ahmet.can@doctor.com', '$2y$10$pass23', '05321000023', 'Beşiktaş/İstanbul', 3),
('Dr. Berrin Demir', 'berrin.demir@doctor.com', '$2y$10$pass24', '05321000024', 'Üsküdar/İstanbul', 3),
('Dr. Cem Kaya', 'cem.kaya@doctor.com', '$2y$10$pass25', '05321000025', 'Şişli/İstanbul', 3),
('Dr. Derya Şahin', 'derya.sahin@doctor.com', '$2y$10$pass26', '05321000026', 'Bakırköy/İstanbul', 3),
('Dr. Eren Arslan', 'eren.arslan@doctor.com', '$2y$10$pass27', '05321000027', 'Fatih/İstanbul', 3),
('Dr. Figen Öztürk', 'figen.ozturk@doctor.com', '$2y$10$pass28', '05321000028', 'Maltepe/İstanbul', 3),

-- İstanbul 5. Hastane Doktorları (56-62)
('Dr. Gökhan Çelik', 'gokhan.celik@doctor.com', '$2y$10$pass29', '05321000029', 'Kadıköy/İstanbul', 3),
('Dr. Hülya Yıldız', 'hulya.yildiz@doctor.com', '$2y$10$pass30', '05321000030', 'Beşiktaş/İstanbul', 3),
('Dr. İsmail Korkmaz', 'ismail.korkmaz@doctor.com', '$2y$10$pass31', '05321000031', 'Üsküdar/İstanbul', 3),
('Dr. Jale Aydın', 'jale.aydin@doctor.com', '$2y$10$pass32', '05321000032', 'Şişli/İstanbul', 3),
('Dr. Kemal Demirtaş', 'kemal.demirtas@doctor.com', '$2y$10$pass33', '05321000033', 'Bakırköy/İstanbul', 3),
('Dr. Leyla Şen', 'leyla.sen@doctor.com', '$2y$10$pass34', '05321000034', 'Fatih/İstanbul', 3),
('Dr. Murat Yılmaz', 'murat.yilmaz@doctor.com', '$2y$10$pass35', '05321000035', 'Maltepe/İstanbul', 3),

-- Ankara 1. Hastane Doktorları (63-69)
('Dr. Nazan Arslan', 'nazan.arslan@doctor.com', '$2y$10$pass36', '05322000001', 'Çankaya/Ankara', 3),
('Dr. Orhan Tekin', 'orhan.tekin@doctor.com', '$2y$10$pass37', '05322000002', 'Keçiören/Ankara', 3),
('Dr. Perihan Akgül', 'perihan.akgul@doctor.com', '$2y$10$pass38', '05322000003', 'Yenimahalle/Ankara', 3),
('Dr. Rıza Şen', 'riza.sen@doctor.com', '$2y$10$pass39', '05322000004', 'Mamak/Ankara', 3),
('Dr. Selma Erdem', 'selma.erdem@doctor.com', '$2y$10$pass40', '05322000005', 'Etimesgut/Ankara', 3),
('Dr. Taner Güler', 'taner.guler@doctor.com', '$2y$10$pass41', '05322000006', 'Sincan/Ankara', 3),
('Dr. Uğur Sarı', 'ugur.sari@doctor.com', '$2y$10$pass42', '05322000007', 'Altındağ/Ankara', 3),

-- Ankara 2. Hastane Doktorları (70-76)
('Dr. Veysel Koç', 'veysel.koc@doctor.com', '$2y$10$pass43', '05322000008', 'Çankaya/Ankara', 3),
('Dr. Yelda Yılmaz', 'yelda.yilmaz@doctor.com', '$2y$10$pass44', '05322000009', 'Keçiören/Ankara', 3),
('Dr. Zafer Can', 'zafer.can@doctor.com', '$2y$10$pass45', '05322000010', 'Yenimahalle/Ankara', 3),
('Dr. Aylin Demir', 'aylin.demir@doctor.com', '$2y$10$pass46', '05322000011', 'Mamak/Ankara', 3),
('Dr. Barış Kaya', 'baris.kaya@doctor.com', '$2y$10$pass47', '05322000012', 'Etimesgut/Ankara', 3),
('Dr. Cansu Şahin', 'cansu.sahin@doctor.com', '$2y$10$pass48', '05322000013', 'Sincan/Ankara', 3),
('Dr. Deniz Arslan', 'deniz.arslan@doctor.com', '$2y$10$pass49', '05322000014', 'Altındağ/Ankara', 3),

-- Ankara 3. Hastane Doktorları (77-83)
('Dr. Ece Öztürk', 'ece.ozturk@doctor.com', '$2y$10$pass50', '05322000015', 'Çankaya/Ankara', 3),
('Dr. Fatih Çelik', 'fatih.celik@doctor.com', '$2y$10$pass51', '05322000016', 'Keçiören/Ankara', 3),
('Dr. Gizem Yıldız', 'gizem.yildiz@doctor.com', '$2y$10$pass52', '05322000017', 'Yenimahalle/Ankara', 3),
('Dr. Hakan Korkmaz', 'hakan.korkmaz@doctor.com', '$2y$10$pass53', '05322000018', 'Mamak/Ankara', 3),
('Dr. Işıl Aydın', 'isil.aydin@doctor.com', '$2y$10$pass54', '05322000019', 'Etimesgut/Ankara', 3),
('Dr. Kaan Demirtaş', 'kaan.demirtas@doctor.com', '$2y$10$pass55', '05322000020', 'Sincan/Ankara', 3),
('Dr. Lale Şen', 'lale.sen@doctor.com', '$2y$10$pass56', '05322000021', 'Altındağ/Ankara', 3),

-- Ankara 4. Hastane Doktorları (84-90)
('Dr. Mehmet Yılmaz', 'mehmet.yilmaz2@doctor.com', '$2y$10$pass57', '05322000022', 'Çankaya/Ankara', 3),
('Dr. Nergis Arslan', 'nergis.arslan@doctor.com', '$2y$10$pass58', '05322000023', 'Keçiören/Ankara', 3),
('Dr. Onur Tekin', 'onur.tekin@doctor.com', '$2y$10$pass59', '05322000024', 'Yenimahalle/Ankara', 3),
('Dr. Pınar Akgül', 'pinar.akgul2@doctor.com', '$2y$10$pass60', '05322000025', 'Mamak/Ankara', 3),
('Dr. Rüya Şen', 'ruya.sen@doctor.com', '$2y$10$pass61', '05322000026', 'Etimesgut/Ankara', 3),
('Dr. Serdar Erdem', 'serdar.erdem@doctor.com', '$2y$10$pass62', '05322000027', 'Sincan/Ankara', 3),
('Dr. Tuğba Güler', 'tugba.guler@doctor.com', '$2y$10$pass63', '05322000028', 'Altındağ/Ankara', 3),

-- Ankara 5. Hastane Doktorları (91-97)
('Dr. Umut Sarı', 'umut.sari@doctor.com', '$2y$10$pass64', '05322000029', 'Çankaya/Ankara', 3),
('Dr. Vuslat Koç', 'vuslat.koc@doctor.com', '$2y$10$pass65', '05322000030', 'Keçiören/Ankara', 3),
('Dr. Yıldız Yılmaz', 'yildiz.yilmaz@doctor.com', '$2y$10$pass66', '05322000031', 'Yenimahalle/Ankara', 3),
('Dr. Zeki Can', 'zeki.can@doctor.com', '$2y$10$pass67', '05322000032', 'Mamak/Ankara', 3),
('Dr. Aslı Demir', 'asli.demir@doctor.com', '$2y$10$pass68', '05322000033', 'Etimesgut/Ankara', 3),
('Dr. Bülent Kaya', 'bulent.kaya@doctor.com', '$2y$10$pass69', '05322000034', 'Sincan/Ankara', 3),
('Dr. Cemile Şahin', 'cemile.sahin@doctor.com', '$2y$10$pass70', '05322000035', 'Altındağ/Ankara', 3),

-- İzmir 1. Hastane Doktorları (98-104)
('Dr. Demet Arslan', 'demet.arslan@doctor.com', '$2y$10$pass71', '05323000001', 'Konak/İzmir', 3),
('Dr. Eren Öztürk', 'eren.ozturk@doctor.com', '$2y$10$pass72', '05323000002', 'Karşıyaka/İzmir', 3),
('Dr. Feride Çelik', 'feride.celik@doctor.com', '$2y$10$pass73', '05323000003', 'Bornova/İzmir', 3),
('Dr. Gökçe Yıldız', 'gokce.yildiz@doctor.com', '$2y$10$pass74', '05323000004', 'Buca/İzmir', 3),
('Dr. Hüseyin Korkmaz', 'huseyin.korkmaz@doctor.com', '$2y$10$pass75', '05323000005', 'Alsancak/İzmir', 3),
('Dr. İrem Aydın', 'irem.aydin@doctor.com', '$2y$10$pass76', '05323000006', 'Gaziemir/İzmir', 3),
('Dr. Jale Demirtaş', 'jale.demirtas@doctor.com', '$2y$10$pass77', '05323000007', 'Balçova/İzmir', 3),

-- İzmir 2. Hastane Doktorları (105-111)
('Dr. Kenan Şen', 'kenan.sen@doctor.com', '$2y$10$pass78', '05323000008', 'Konak/İzmir', 3),
('Dr. Leman Arslan', 'leman.arslan@doctor.com', '$2y$10$pass79', '05323000009', 'Karşıyaka/İzmir', 3),
('Dr. Mesut Tekin', 'mesut.tekin@doctor.com', '$2y$10$pass80', '05323000010', 'Bornova/İzmir', 3),
('Dr. Nihal Akgül', 'nihal.akgul@doctor.com', '$2y$10$pass81', '05323000011', 'Buca/İzmir', 3),
('Dr. Oya Şen', 'oya.sen@doctor.com', '$2y$10$pass82', '05323000012', 'Alsancak/İzmir', 3),
('Dr. Ömer Erdem', 'omer.erdem@doctor.com', '$2y$10$pass83', '05323000013', 'Gaziemir/İzmir', 3),
('Dr. Pervin Güler', 'pervin.guler@doctor.com', '$2y$10$pass84', '05323000014', 'Balçova/İzmir', 3),

-- İzmir 3. Hastane Doktorları (112-118)
('Dr. Raşit Sarı', 'rasit.sari@doctor.com', '$2y$10$pass85', '05323000015', 'Konak/İzmir', 3),
('Dr. Sevgi Koç', 'sevgi.koc@doctor.com', '$2y$10$pass86', '05323000016', 'Karşıyaka/İzmir', 3),
('Dr. Tolga Yılmaz', 'tolga.yilmaz@doctor.com', '$2y$10$pass87', '05323000017', 'Bornova/İzmir', 3),
('Dr. Ufuk Can', 'ufuk.can@doctor.com', '$2y$10$pass88', '05323000018', 'Buca/İzmir', 3),
('Dr. Veli Demir', 'veli.demir@doctor.com', '$2y$10$pass89', '05323000019', 'Alsancak/İzmir', 3),
('Dr. Yasemin Kaya', 'yasemin.kaya@doctor.com', '$2y$10$pass90', '05323000020', 'Gaziemir/İzmir', 3),
('Dr. Zuhal Şahin', 'zuhal.sahin@doctor.com', '$2y$10$pass91', '05323000021', 'Balçova/İzmir', 3),

-- İzmir 4. Hastane Doktorları (119-125)
('Dr. Abdullah Arslan', 'abdullah.arslan@doctor.com', '$2y$10$pass92', '05323000022', 'Konak/İzmir', 3),
('Dr. Beyza Öztürk', 'beyza.ozturk@doctor.com', '$2y$10$pass93', '05323000023', 'Karşıyaka/İzmir', 3),
('Dr. Cengiz Çelik', 'cengiz.celik@doctor.com', '$2y$10$pass94', '05323000024', 'Bornova/İzmir', 3),
('Dr. Dilan Yıldız', 'dilan.yildiz@doctor.com', '$2y$10$pass95', '05323000025', 'Buca/İzmir', 3),
('Dr. Emir Korkmaz', 'emir.korkmaz@doctor.com', '$2y$10$pass96', '05323000026', 'Alsancak/İzmir', 3),
('Dr. Fadime Aydın', 'fadime.aydin@doctor.com', '$2y$10$pass97', '05323000027', 'Gaziemir/İzmir', 3),
('Dr. Gönül Demirtaş', 'gonul.demirtas@doctor.com', '$2y$10$pass98', '05323000028', 'Balçova/İzmir', 3),

-- İzmir 5. Hastane Doktorları (126-132)
('Dr. Hikmet Şen', 'hikmet.sen@doctor.com', '$2y$10$pass99', '05323000029', 'Konak/İzmir', 3),
('Dr. Işık Arslan', 'isik.arslan@doctor.com', '$2y$10$pass100', '05323000030', 'Karşıyaka/İzmir', 3),
('Dr. Jale Tekin', 'jale.tekin@doctor.com', '$2y$10$pass101', '05323000031', 'Bornova/İzmir', 3),
('Dr. Kadir Akgül', 'kadir.akgul@doctor.com', '$2y$10$pass102', '05323000032', 'Buca/İzmir', 3),
('Dr. Lerzan Şen', 'lerzan.sen@doctor.com', '$2y$10$pass103', '05323000033', 'Alsancak/İzmir', 3),
('Dr. Mert Erdem', 'mert.erdem@doctor.com', '$2y$10$pass104', '05323000034', 'Gaziemir/İzmir', 3),
('Dr. Nalan Güler', 'nalan.guler@doctor.com', '$2y$10$pass105', '05323000035', 'Balçova/İzmir', 3),

-- Bursa 1. Hastane Doktorları (133-139)
('Dr. Okan Sarı', 'okan.sari@doctor.com', '$2y$10$pass106', '05324000001', 'Osmangazi/Bursa', 3),
('Dr. Pınar Koç', 'pinar.koc@doctor.com', '$2y$10$pass107', '05324000002', 'Nilüfer/Bursa', 3),
('Dr. Rıza Yılmaz', 'riza.yilmaz@doctor.com', '$2y$10$pass108', '05324000003', 'Yıldırım/Bursa', 3),
('Dr. Sibel Can', 'sibel.can@doctor.com', '$2y$10$pass109', '05324000004', 'Gemlik/Bursa', 3),
('Dr. Taner Demir', 'taner.demir@doctor.com', '$2y$10$pass110', '05324000005', 'İnegöl/Bursa', 3),
('Dr. Uğur Kaya', 'ugur.kaya@doctor.com', '$2y$10$pass111', '05324000006', 'Mustafakemalpaşa/Bursa', 3),
('Dr. Vildan Şahin', 'vildan.sahin@doctor.com', '$2y$10$pass112', '05324000007', 'Karacabey/Bursa', 3),

-- Bursa 2. Hastane Doktorları (140-146)
('Dr. Yavuz Arslan', 'yavuz.arslan@doctor.com', '$2y$10$pass113', '05324000008', 'Osmangazi/Bursa', 3),
('Dr. Zeynep Öztürk', 'zeynep.ozturk2@doctor.com', '$2y$10$pass114', '05324000009', 'Nilüfer/Bursa', 3),
('Dr. Alper Çelik', 'alper.celik@doctor.com', '$2y$10$pass115', '05324000010', 'Yıldırım/Bursa', 3),
('Dr. Banu Yıldız', 'banu.yildiz@doctor.com', '$2y$10$pass116', '05324000011', 'Gemlik/Bursa', 3),
('Dr. Cemal Korkmaz', 'cemal.korkmaz@doctor.com', '$2y$10$pass117', '05324000012', 'İnegöl/Bursa', 3),
('Dr. Derya Aydın', 'derya.aydin@doctor.com', '$2y$10$pass118', '05324000013', 'Mustafakemalpaşa/Bursa', 3),
('Dr. Erhan Demirtaş', 'erhan.demirtas@doctor.com', '$2y$10$pass119', '05324000014', 'Karacabey/Bursa', 3),

-- Bursa 3. Hastane Doktorları (147-153)
('Dr. Fulya Şen', 'fulya.sen@doctor.com', '$2y$10$pass120', '05324000015', 'Osmangazi/Bursa', 3),
('Dr. Gürkan Arslan', 'gurkan.arslan@doctor.com', '$2y$10$pass121', '05324000016', 'Nilüfer/Bursa', 3),
('Dr. Hüma Tekin', 'huma.tekin@doctor.com', '$2y$10$pass122', '05324000017', 'Yıldırım/Bursa', 3),
('Dr. İlker Akgül', 'ilker.akgul@doctor.com', '$2y$10$pass123', '05324000018', 'Gemlik/Bursa', 3),
('Dr. Jale Şen', 'jale.sen2@doctor.com', '$2y$10$pass124', '05324000019', 'İnegöl/Bursa', 3),
('Dr. Kerim Erdem', 'kerim.erdem@doctor.com', '$2y$10$pass125', '05324000020', 'Mustafakemalpaşa/Bursa', 3),
('Dr. Lütfiye Güler', 'lutfiye.guler@doctor.com', '$2y$10$pass126', '05324000021', 'Karacabey/Bursa', 3),

-- Bursa 4. Hastane Doktorları (154-160)
('Dr. Mehmet Sarı', 'mehmet.sari@doctor.com', '$2y$10$pass127', '05324000022', 'Osmangazi/Bursa', 3),
('Dr. Nermin Koç', 'nermin.koc@doctor.com', '$2y$10$pass128', '05324000023', 'Nilüfer/Bursa', 3),
('Dr. Orhan Yılmaz', 'orhan.yilmaz2@doctor.com', '$2y$10$pass129', '05324000024', 'Yıldırım/Bursa', 3),
('Dr. Perihan Can', 'perihan.can@doctor.com', '$2y$10$pass130', '05324000025', 'Gemlik/Bursa', 3),
('Dr. Rüstem Demir', 'rustem.demir@doctor.com', '$2y$10$pass131', '05324000026', 'İnegöl/Bursa', 3),
('Dr. Sevim Kaya', 'sevim.kaya@doctor.com', '$2y$10$pass132', '05324000027', 'Mustafakemalpaşa/Bursa', 3),
('Dr. Turgut Şahin', 'turgut.sahin@doctor.com', '$2y$10$pass133', '05324000028', 'Karacabey/Bursa', 3),

-- Bursa 5. Hastane Doktorları (161-167)
('Dr. Ummuhan Arslan', 'ummuhan.arslan@doctor.com', '$2y$10$pass134', '05324000029', 'Osmangazi/Bursa', 3),
('Dr. Veli Öztürk', 'veli.ozturk@doctor.com', '$2y$10$pass135', '05324000030', 'Nilüfer/Bursa', 3),
('Dr. Yıldız Çelik', 'yildiz.celik@doctor.com', '$2y$10$pass136', '05324000031', 'Yıldırım/Bursa', 3),
('Dr. Zeki Yıldız', 'zeki.yildiz@doctor.com', '$2y$10$pass137', '05324000032', 'Gemlik/Bursa', 3),
('Dr. Asuman Korkmaz', 'asuman.korkmaz@doctor.com', '$2y$10$pass138', '05324000033', 'İnegöl/Bursa', 3),
('Dr. Burak Aydın', 'burak.aydin@doctor.com', '$2y$10$pass139', '05324000034', 'Mustafakemalpaşa/Bursa', 3),
('Dr. Candan Demirtaş', 'candan.demirtas@doctor.com', '$2y$10$pass140', '05324000035', 'Karacabey/Bursa', 3),

-- Antalya 1. Hastane Doktorları (168-174)
('Dr. Deniz Şen', 'deniz.sen@doctor.com', '$2y$10$pass141', '05325000001', 'Muratpaşa/Antalya', 3),
('Dr. Erol Arslan', 'erol.arslan@doctor.com', '$2y$10$pass142', '05325000002', 'Konyaaltı/Antalya', 3),
('Dr. Figen Tekin', 'figen.tekin@doctor.com', '$2y$10$pass143', '05325000003', 'Kepez/Antalya', 3),
('Dr. Gül Akgül', 'gul.akgul@doctor.com', '$2y$10$pass144', '05325000004', 'Lara/Antalya', 3),
('Dr. Hakan Şen', 'hakan.sen2@doctor.com', '$2y$10$pass145', '05325000005', 'Alanya/Antalya', 3),
('Dr. İpek Erdem', 'ipek.erdem@doctor.com', '$2y$10$pass146', '05325000006', 'Manavgat/Antalya', 3),
('Dr. Koray Güler', 'koray.guler@doctor.com', '$2y$10$pass147', '05325000007', 'Serik/Antalya', 3),

-- Antalya 2. Hastane Doktorları (175-181)
('Dr. Lale Sarı', 'lale.sari@doctor.com', '$2y$10$pass148', '05325000008', 'Muratpaşa/Antalya', 3),
('Dr. Mesut Koç', 'mesut.koc@doctor.com', '$2y$10$pass149', '05325000009', 'Konyaaltı/Antalya', 3),
('Dr. Nurdan Yılmaz', 'nurdan.yilmaz@doctor.com', '$2y$10$pass150', '05325000010', 'Kepez/Antalya', 3),
('Dr. Oğuz Can', 'oguz.can@doctor.com', '$2y$10$pass151', '05325000011', 'Lara/Antalya', 3),
('Dr. Pınar Demir', 'pinar.demir@doctor.com', '$2y$10$pass152', '05325000012', 'Alanya/Antalya', 3),
('Dr. Ramazan Kaya', 'ramazan.kaya@doctor.com', '$2y$10$pass153', '05325000013', 'Manavgat/Antalya', 3),
('Dr. Sema Şahin', 'sema.sahin@doctor.com', '$2y$10$pass154', '05325000014', 'Serik/Antalya', 3),

-- Antalya 3. Hastane Doktorları (182-188)
('Dr. Tarık Arslan', 'tarik.arslan@doctor.com', '$2y$10$pass155', '05325000015', 'Muratpaşa/Antalya', 3),
('Dr. Umay Öztürk', 'umay.ozturk@doctor.com', '$2y$10$pass156', '05325000016', 'Konyaaltı/Antalya', 3),
('Dr. Volkan Çelik', 'volkan.celik@doctor.com', '$2y$10$pass157', '05325000017', 'Kepez/Antalya', 3),
('Dr. Yonca Yıldız', 'yonca.yildiz@doctor.com', '$2y$10$pass158', '05325000018', 'Lara/Antalya', 3),
('Dr. Zafer Korkmaz', 'zafer.korkmaz@doctor.com', '$2y$10$pass159', '05325000019', 'Alanya/Antalya', 3),
('Dr. Aysun Aydın', 'aysun.aydin@doctor.com', '$2y$10$pass160', '05325000020', 'Manavgat/Antalya', 3),
('Dr. Bülent Demirtaş', 'bulent.demirtas@doctor.com', '$2y$10$pass161', '05325000021', 'Serik/Antalya', 3),

-- Antalya 4. Hastane Doktorları (189-195)
('Dr. Cemre Şen', 'cemre.sen@doctor.com', '$2y$10$pass162', '05325000022', 'Muratpaşa/Antalya', 3),
('Dr. Durmuş Arslan', 'durmus.arslan@doctor.com', '$2y$10$pass163', '05325000023', 'Konyaaltı/Antalya', 3),
('Dr. Elvan Tekin', 'elvan.tekin@doctor.com', '$2y$10$pass164', '05325000024', 'Kepez/Antalya', 3),
('Dr. Fuat Akgül', 'fuat.akgul@doctor.com', '$2y$10$pass165', '05325000025', 'Lara/Antalya', 3),
('Dr. Gönül Şen', 'gonul.sen@doctor.com', '$2y$10$pass166', '05325000026', 'Alanya/Antalya', 3),
('Dr. Hüseyin Erdem', 'huseyin.erdem@doctor.com', '$2y$10$pass167', '05325000027', 'Manavgat/Antalya', 3),
('Dr. İclal Güler', 'iclal.guler@doctor.com', '$2y$10$pass168', '05325000028', 'Serik/Antalya', 3),

-- Antalya 5. Hastane Doktorları (196-202)
('Dr. Kadir Sarı', 'kadir.sari@doctor.com', '$2y$10$pass169', '05325000029', 'Muratpaşa/Antalya', 3),
('Dr. Lerzan Koç', 'lerzan.koc@doctor.com', '$2y$10$pass170', '05325000030', 'Konyaaltı/Antalya', 3),
('Dr. Mithat Yılmaz', 'mithat.yilmaz@doctor.com', '$2y$10$pass171', '05325000031', 'Kepez/Antalya', 3),
('Dr. Neriman Can', 'neriman.can@doctor.com', '$2y$10$pass172', '05325000032', 'Lara/Antalya', 3),
('Dr. Oya Demir', 'oya.demir@doctor.com', '$2y$10$pass173', '05325000033', 'Alanya/Antalya', 3),
('Dr. Recep Kaya', 'recep.kaya@doctor.com', '$2y$10$pass174', '05325000034', 'Manavgat/Antalya', 3),
('Dr. Sibel Şahin', 'sibel.sahin@doctor.com', '$2y$10$pass175', '05325000035', 'Serik/Antalya', 3);

-- Hastalar (user_id: 203-377, 175 hasta - örnek olarak sadece ilk 10'u gösteriliyor)
INSERT INTO User (name, email, password, phone, address, role_id) VALUES
                                                                      ('Kemal Taş', 'kemal.tas@gmail.com', '$2y$10$hasta1', '05332345678', 'Esenler/İstanbul', 4),
                                                                      ('Merve Çakır', 'merve.cakir@gmail.com', '$2y$10$hasta2', '05433456789', 'Keçiören/Ankara', 4),
                                                                      ('Burak Yalçın', 'burak.yalcin@gmail.com', '$2y$10$hasta3', '05344567890', 'Kartal/İstanbul', 4),
                                                                      ('Nazlı Kara', 'nazli.kara@gmail.com', '$2y$10$hasta4', '05445678901', 'Bornova/İzmir', 4),
                                                                      ('Okan Tekin', 'okan.tekin@gmail.com', '$2y$10$hasta5', '05356789012', 'Pendik/İstanbul', 4),
                                                                      ('Pınar Güneş', 'pinar.gunes@gmail.com', '$2y$10$hasta6', '05457890123', 'Nilüfer/Bursa', 4),
                                                                      ('Serkan Ateş', 'serkan.ates@gmail.com', '$2y$10$hasta7', '05368901234', 'Kadıköy/İstanbul', 4),
                                                                      ('Tuğba Vural', 'tugba.vural@gmail.com', '$2y$10$hasta8', '05469012345', 'Karşıyaka/İzmir', 4),
                                                                      ('Ufuk Özkan', 'ufuk.ozkan@gmail.com', '$2y$10$hasta9', '05370123456', 'Beşiktaş/İstanbul', 4),
                                                                      ('Vildan Şen', 'vildan.sen@gmail.com', '$2y$10$hasta10', '05471234567', 'Çankaya/Ankara', 4),
                                                                      ('Yusuf Acar', 'yusuf.acar@gmail.com', '$2y$10$hasta11', '05382345678', 'Maltepe/İstanbul', 4),
                                                                      ('Zehra Öz', 'zehra.oz@gmail.com', '$2y$10$hasta12', '05483456789', 'Bayraklı/İzmir', 4),
                                                                      ('Ali Demir', 'ali.demir@gmail.com', '$2y$10$hasta13', '05394567890', 'Üsküdar/İstanbul', 4),
                                                                      ('Aylin Yurt', 'aylin.yurt@gmail.com', '$2y$10$hasta14', '05495678901', 'Alsancak/İzmir', 4),
                                                                      ('Berk Can', 'berk.can@gmail.com', '$2y$10$hasta15', '05306789012', 'Fatih/İstanbul', 4),
                                                                      ('Ceren Bulut', 'ceren.bulut@gmail.com', '$2y$10$hasta16', '05507890123', 'Gaziemir/İzmir', 4),
                                                                      ('Deniz Eren', 'deniz.eren@gmail.com', '$2y$10$hasta17', '05318901234', 'Şişli/İstanbul', 4),
                                                                      ('Ece Kaya', 'ece.kaya@gmail.com', '$2y$10$hasta18', '05419012345', 'Çukurambar/Ankara', 4),
                                                                      ('Fatih Koç', 'fatih.koc@gmail.com', '$2y$10$hasta19', '05320123456', 'Beyoğlu/İstanbul', 4),
                                                                      ('Gül Arslan', 'gul.arslan@gmail.com', '$2y$10$hasta20', '05421234567', 'Kartal/İstanbul', 4),

                                                                      ('Hasan Öztürk', 'hasan.ozturk@gmail.com', '$2y$10$hasta21', '05331234568', 'Kepez/Antalya', 4),
                                                                      ('İpek Yılmaz', 'ipek.yilmaz@gmail.com', '$2y$10$hasta22', '05442345679', 'Osmangazi/Bursa', 4),
                                                                      ('Kaan Şahin', 'kaan.sahin@gmail.com', '$2y$10$hasta23', '05353456780', 'Lara/Antalya', 4),
                                                                      ('Lara Polat', 'lara.polat@gmail.com', '$2y$10$hasta24', '05454567891', 'Mamak/Ankara', 4),
                                                                      ('Mert Aydın', 'mert.aydin@gmail.com', '$2y$10$hasta25', '05365678902', 'Buca/İzmir', 4),
                                                                      ('Nihan Kurt', 'nihan.kurt@gmail.com', '$2y$10$hasta26', '05466789013', 'Çekirge/Bursa', 4),
                                                                      ('Oğuz Tekin', 'oguz.tekin@gmail.com', '$2y$10$hasta27', '05377890124', 'Konyaaltı/Antalya', 4),
                                                                      ('Özlem Yıldız', 'ozlem.yildiz@gmail.com', '$2y$10$hasta28', '05478901235', 'Sincan/Ankara', 4),
                                                                      ('Ramazan Aksoy', 'ramazan.aksoy@gmail.com', '$2y$10$hasta29', '05389012346', 'Ümraniye/İstanbul', 4),
                                                                      ('Seda Çelik', 'seda.celik@gmail.com', '$2y$10$hasta30', '05490123457', 'Narlıdere/İzmir', 4),

                                                                      ('Tamer Özer', 'tamer.ozer@gmail.com', '$2y$10$hasta31', '05301234568', 'Muratpaşa/Antalya', 4),
                                                                      ('Ümit Şimşek', 'umit.simsek@gmail.com', '$2y$10$hasta32', '05512345679', 'Etimesgut/Ankara', 4),
                                                                      ('Volkan Güler', 'volkan.guler@gmail.com', '$2y$10$hasta33', '05323456780', 'Sultanbeyli/İstanbul', 4),
                                                                      ('Yasemin Avcı', 'yasemin.avci@gmail.com', '$2y$10$hasta34', '05434567891', 'Balçova/İzmir', 4),
                                                                      ('Zafer Konak', 'zafer.konak@gmail.com', '$2y$10$hasta35', '05345678902', 'Yıldırım/Bursa', 4),
                                                                      ('Arda Yılmaz', 'arda.yilmaz@gmail.com', '$2y$10$hasta36', '05456789013', 'Çankaya/Ankara', 4),
                                                                      ('Bade Şahin', 'bade.sahin@gmail.com', '$2y$10$hasta37', '05367890124', 'Kadıköy/İstanbul', 4),
                                                                      ('Cemre Demir', 'cemre.demir@gmail.com', '$2y$10$hasta38', '05478901235', 'Bornova/İzmir', 4),
                                                                      ('Doruk Kaya', 'doruk.kaya@gmail.com', '$2y$10$hasta39', '05389012346', 'Nilüfer/Bursa', 4),
                                                                      ('Ebru Arslan', 'ebru.arslan@gmail.com', '$2y$10$hasta40', '05490123457', 'Muratpaşa/Antalya', 4),

                                                                      ('Fırat Öztürk', 'firat.ozturk@gmail.com', '$2y$10$hasta41', '05301234568', 'Beşiktaş/İstanbul', 4),
                                                                      ('Gamze Çelik', 'gamze.celik@gmail.com', '$2y$10$hasta42', '05512345679', 'Keçiören/Ankara', 4),
                                                                      ('Hakan Yıldız', 'hakan.yildiz@gmail.com', '$2y$10$hasta43', '05323456780', 'Karşıyaka/İzmir', 4),
                                                                      ('Irmak Korkmaz', 'irmak.korkmaz@gmail.com', '$2y$10$hasta44', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Jale Aydın', 'jale.aydin@gmail.com', '$2y$10$hasta45', '05345678902', 'Konyaaltı/Antalya', 4),
                                                                      ('Koray Demirtaş', 'koray.demirtas@gmail.com', '$2y$10$hasta46', '05456789013', 'Üsküdar/İstanbul', 4),
                                                                      ('Leyla Şen', 'leyla.sen@gmail.com', '$2y$10$hasta47', '05367890124', 'Yenimahalle/Ankara', 4),
                                                                      ('Mete Güler', 'mete.guler@gmail.com', '$2y$10$hasta48', '05478901235', 'Buca/İzmir', 4),
                                                                      ('Naz Sarı', 'naz.sari@gmail.com', '$2y$10$hasta49', '05389012346', 'Yıldırım/Bursa', 4),
                                                                      ('Onur Koç', 'onur.koc@gmail.com', '$2y$10$hasta50', '05490123457', 'Lara/Antalya', 4),

                                                                      ('Pelin Yılmaz', 'pelin.yilmaz@gmail.com', '$2y$10$hasta51', '05301234568', 'Şişli/İstanbul', 4),
                                                                      ('Rıza Can', 'riza.can@gmail.com', '$2y$10$hasta52', '05512345679', 'Mamak/Ankara', 4),
                                                                      ('Selin Demir', 'selin.demir@gmail.com', '$2y$10$hasta53', '05323456780', 'Konak/İzmir', 4),
                                                                      ('Tolga Kaya', 'tolga.kaya@gmail.com', '$2y$10$hasta54', '05434567891', 'Nilüfer/Bursa', 4),
                                                                      ('Umut Şahin', 'umut.sahin@gmail.com', '$2y$10$hasta55', '05345678902', 'Kepez/Antalya', 4),
                                                                      ('Vedat Arslan', 'vedat.arslan@gmail.com', '$2y$10$hasta56', '05456789013', 'Bakırköy/İstanbul', 4),
                                                                      ('Yıldız Öztürk', 'yildiz.ozturk@gmail.com', '$2y$10$hasta57', '05367890124', 'Etimesgut/Ankara', 4),
                                                                      ('Zerrin Çelik', 'zerrin.celik@gmail.com', '$2y$10$hasta58', '05478901235', 'Alsancak/İzmir', 4),
                                                                      ('Alper Yıldız', 'alper.yildiz@gmail.com', '$2y$10$hasta59', '05389012346', 'Gemlik/Bursa', 4),
                                                                      ('Burcu Korkmaz', 'burcu.korkmaz@gmail.com', '$2y$10$hasta60', '05490123457', 'Alanya/Antalya', 4),

                                                                      ('Candan Aydın', 'candan.aydin@gmail.com', '$2y$10$hasta61', '05301234568', 'Fatih/İstanbul', 4),
                                                                      ('Davut Demirtaş', 'davut.demirtas@gmail.com', '$2y$10$hasta62', '05512345679', 'Sincan/Ankara', 4),
                                                                      ('Elif Şen', 'elif.sen@gmail.com', '$2y$10$hasta63', '05323456780', 'Karşıyaka/İzmir', 4),
                                                                      ('Fikret Güler', 'fikret.guler@gmail.com', '$2y$10$hasta64', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Gönül Sarı', 'gonul.sari@gmail.com', '$2y$10$hasta65', '05345678902', 'Muratpaşa/Antalya', 4),
                                                                      ('Hüseyin Koç', 'huseyin.koc@gmail.com', '$2y$10$hasta66', '05456789013', 'Kadıköy/İstanbul', 4),
                                                                      ('İnci Yılmaz', 'inci.yilmaz@gmail.com', '$2y$10$hasta67', '05367890124', 'Çankaya/Ankara', 4),
                                                                      ('Kadir Can', 'kadir.can@gmail.com', '$2y$10$hasta68', '05478901235', 'Bornova/İzmir', 4),
                                                                      ('Lale Demir', 'lale.demir@gmail.com', '$2y$10$hasta69', '05389012346', 'İnegöl/Bursa', 4),
                                                                      ('Mehmet Kaya', 'mehmet.kaya@gmail.com', '$2y$10$hasta70', '05490123457', 'Manavgat/Antalya', 4),

                                                                      ('Nevin Şahin', 'nevin.sahin@gmail.com', '$2y$10$hasta71', '05301234568', 'Ümraniye/İstanbul', 4),
                                                                      ('Orhan Arslan', 'orhan.arslan@gmail.com', '$2y$10$hasta72', '05512345679', 'Keçiören/Ankara', 4),
                                                                      ('Perihan Öztürk', 'perihan.ozturk@gmail.com', '$2y$10$hasta73', '05323456780', 'Buca/İzmir', 4),
                                                                      ('Recep Çelik', 'recep.celik@gmail.com', '$2y$10$hasta74', '05434567891', 'Yıldırım/Bursa', 4),
                                                                      ('Süreyya Yıldız', 'sureyya.yildiz@gmail.com', '$2y$10$hasta75', '05345678902', 'Konyaaltı/Antalya', 4),
                                                                      ('Türkan Korkmaz', 'turkan.korkmaz@gmail.com', '$2y$10$hasta76', '05456789013', 'Beşiktaş/İstanbul', 4),
                                                                      ('Uğur Aydın', 'ugur.aydin@gmail.com', '$2y$10$hasta77', '05367890124', 'Yenimahalle/Ankara', 4),
                                                                      ('Veli Demirtaş', 'veli.demirtas@gmail.com', '$2y$10$hasta78', '05478901235', 'Gaziemir/İzmir', 4),
                                                                      ('Yunus Şen', 'yunus.sen@gmail.com', '$2y$10$hasta79', '05389012346', 'Mustafakemalpaşa/Bursa', 4),
                                                                      ('Zehra Güler', 'zehra.guler@gmail.com', '$2y$10$hasta80', '05490123457', 'Serik/Antalya', 4),

                                                                      ('Ahmet Sarı', 'ahmet.sari@gmail.com', '$2y$10$hasta81', '05301234568', 'Maltepe/İstanbul', 4),
                                                                      ('Bilge Koç', 'bilge.koc@gmail.com', '$2y$10$hasta82', '05512345679', 'Mamak/Ankara', 4),
                                                                      ('Cem Yılmaz', 'cem.yilmaz@gmail.com', '$2y$10$hasta83', '05323456780', 'Karşıyaka/İzmir', 4),
                                                                      ('Dilek Can', 'dilek.can@gmail.com', '$2y$10$hasta84', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Emre Demir', 'emre.demir@gmail.com', '$2y$10$hasta85', '05345678902', 'Lara/Antalya', 4),
                                                                      ('Fadime Kaya', 'fadime.kaya@gmail.com', '$2y$10$hasta86', '05456789013', 'Şişli/İstanbul', 4),
                                                                      ('Gökhan Şahin', 'gokhan.sahin@gmail.com', '$2y$10$hasta87', '05367890124', 'Etimesgut/Ankara', 4),
                                                                      ('Hülya Arslan', 'hulya.arslan@gmail.com', '$2y$10$hasta88', '05478901235', 'Konak/İzmir', 4),
                                                                      ('İbrahim Öztürk', 'ibrahim.ozturk@gmail.com', '$2y$10$hasta89', '05389012346', 'Nilüfer/Bursa', 4),
                                                                      ('Jale Çelik', 'jale.celik@gmail.com', '$2y$10$hasta90', '05490123457', 'Kepez/Antalya', 4),

                                                                      ('Kıvanç Yıldız', 'kivanc.yildiz@gmail.com', '$2y$10$hasta91', '05301234568', 'Bakırköy/İstanbul', 4),
                                                                      ('Leyla Korkmaz', 'leyla.korkmaz@gmail.com', '$2y$10$hasta92', '05512345679', 'Çankaya/Ankara', 4),
                                                                      ('Murat Aydın', 'murat.aydin@gmail.com', '$2y$10$hasta93', '05323456780', 'Bornova/İzmir', 4),
                                                                      ('Nermin Demirtaş', 'nermin.demirtas@gmail.com', '$2y$10$hasta94', '05434567891', 'Gemlik/Bursa', 4),
                                                                      ('Osman Şen', 'osman.sen@gmail.com', '$2y$10$hasta95', '05345678902', 'Alanya/Antalya', 4),
                                                                      ('Pınar Güler', 'pinar.guler@gmail.com', '$2y$10$hasta96', '05456789013', 'Kadıköy/İstanbul', 4),
                                                                      ('Rahmi Sarı', 'rahmi.sari@gmail.com', '$2y$10$hasta97', '05367890124', 'Keçiören/Ankara', 4),
                                                                      ('Sibel Koç', 'sibel.koc@gmail.com', '$2y$10$hasta98', '05478901235', 'Karşıyaka/İzmir', 4),
                                                                      ('Tayfun Yılmaz', 'tayfun.yilmaz@gmail.com', '$2y$10$hasta99', '05389012346', 'Yıldırım/Bursa', 4),
                                                                      ('Ufuk Can', 'ufuk.can@gmail.com', '$2y$10$hasta100', '05490123457', 'Muratpaşa/Antalya', 4),

                                                                      ('Vedat Demir', 'vedat.demir@gmail.com', '$2y$10$hasta101', '05301234568', 'Üsküdar/İstanbul', 4),
                                                                      ('Yasemin Kaya', 'yasemin.kaya@gmail.com', '$2y$10$hasta102', '05512345679', 'Yenimahalle/Ankara', 4),
                                                                      ('Zeki Şahin', 'zeki.sahin@gmail.com', '$2y$10$hasta103', '05323456780', 'Buca/İzmir', 4),
                                                                      ('Aslı Arslan', 'asli.arslan@gmail.com', '$2y$10$hasta104', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Burak Öztürk', 'burak.ozturk@gmail.com', '$2y$10$hasta105', '05345678902', 'Konyaaltı/Antalya', 4),
                                                                      ('Ceyda Çelik', 'ceyda.celik@gmail.com', '$2y$10$hasta106', '05456789013', 'Beşiktaş/İstanbul', 4),
                                                                      ('Deniz Yıldız', 'deniz.yildiz@gmail.com', '$2y$10$hasta107', '05367890124', 'Mamak/Ankara', 4),
                                                                      ('Erdem Korkmaz', 'erdem.korkmaz@gmail.com', '$2y$10$hasta108', '05478901235', 'Alsancak/İzmir', 4),
                                                                      ('Filiz Aydın', 'filiz.aydin@gmail.com', '$2y$10$hasta109', '05389012346', 'İnegöl/Bursa', 4),
                                                                      ('Gürkan Demirtaş', 'gurkan.demirtas@gmail.com', '$2y$10$hasta110', '05490123457', 'Manavgat/Antalya', 4),

                                                                      ('Hüma Şen', 'huma.sen@gmail.com', '$2y$10$hasta111', '05301234568', 'Fatih/İstanbul', 4),
                                                                      ('İlker Güler', 'ilker.guler@gmail.com', '$2y$10$hasta112', '05512345679', 'Sincan/Ankara', 4),
                                                                      ('Jale Sarı', 'jale.sari@gmail.com', '$2y$10$hasta113', '05323456780', 'Konak/İzmir', 4),
                                                                      ('Kemal Koç', 'kemal.koc@gmail.com', '$2y$10$hasta114', '05434567891', 'Nilüfer/Bursa', 4),
                                                                      ('Lütfiye Yılmaz', 'lutfiye.yilmaz@gmail.com', '$2y$10$hasta115', '05345678902', 'Kepez/Antalya', 4),
                                                                      ('Mithat Can', 'mithat.can@gmail.com', '$2y$10$hasta116', '05456789013', 'Şişli/İstanbul', 4),
                                                                      ('Nurdan Demir', 'nurdan.demir@gmail.com', '$2y$10$hasta117', '05367890124', 'Etimesgut/Ankara', 4),
                                                                      ('Oya Kaya', 'oya.kaya@gmail.com', '$2y$10$hasta118', '05478901235', 'Gaziemir/İzmir', 4),
                                                                      ('Ömer Şahin', 'omer.sahin@gmail.com', '$2y$10$hasta119', '05389012346', 'Gemlik/Bursa', 4),
                                                                      ('Pervin Arslan', 'pervin.arslan@gmail.com', '$2y$10$hasta120', '05490123457', 'Alanya/Antalya', 4),

                                                                      ('Rıfat Öztürk', 'rifat.ozturk@gmail.com', '$2y$10$hasta121', '05301234568', 'Bakırköy/İstanbul', 4),
                                                                      ('Sultan Çelik', 'sultan.celik@gmail.com', '$2y$10$hasta122', '05512345679', 'Çankaya/Ankara', 4),
                                                                      ('Tevfik Yıldız', 'tevfik.yildiz@gmail.com', '$2y$10$hasta123', '05323456780', 'Karşıyaka/İzmir', 4),
                                                                      ('Ulviye Korkmaz', 'ulviye.korkmaz@gmail.com', '$2y$10$hasta124', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Vahit Aydın', 'vahit.aydin@gmail.com', '$2y$10$hasta125', '05345678902', 'Lara/Antalya', 4),
                                                                      ('Yeter Demirtaş', 'yeter.demirtas@gmail.com', '$2y$10$hasta126', '05456789013', 'Kadıköy/İstanbul', 4),
                                                                      ('Zühtü Şen', 'zuhtu.sen@gmail.com', '$2y$10$hasta127', '05367890124', 'Keçiören/Ankara', 4),
                                                                      ('Altan Güler', 'altan.guler@gmail.com', '$2y$10$hasta128', '05478901235', 'Bornova/İzmir', 4),
                                                                      ('Binnur Sarı', 'binnur.sari@gmail.com', '$2y$10$hasta129', '05389012346', 'Yıldırım/Bursa', 4),
                                                                      ('Cemal Koç', 'cemal.koc@gmail.com', '$2y$10$hasta130', '05490123457', 'Muratpaşa/Antalya', 4),

                                                                      ('Derya Yılmaz', 'derya.yilmaz@gmail.com', '$2y$10$hasta131', '05301234568', 'Ümraniye/İstanbul', 4),
                                                                      ('Ercan Can', 'ercan.can@gmail.com', '$2y$10$hasta132', '05512345679', 'Yenimahalle/Ankara', 4),
                                                                      ('Füsun Demir', 'fusun.demir@gmail.com', '$2y$10$hasta133', '05323456780', 'Buca/İzmir', 4),
                                                                      ('Gökçe Kaya', 'gokce.kaya@gmail.com', '$2y$10$hasta134', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Hasan Şahin', 'hasan.sahin@gmail.com', '$2y$10$hasta135', '05345678902', 'Konyaaltı/Antalya', 4),
                                                                      ('İclal Arslan', 'iclal.arslan@gmail.com', '$2y$10$hasta136', '05456789013', 'Beşiktaş/İstanbul', 4),
                                                                      ('Kadir Öztürk', 'kadir.ozturk@gmail.com', '$2y$10$hasta137', '05367890124', 'Mamak/Ankara', 4),
                                                                      ('Lale Çelik', 'lale.celik@gmail.com', '$2y$10$hasta138', '05478901235', 'Alsancak/İzmir', 4),
                                                                      ('Mehtap Yıldız', 'mehtap.yildiz@gmail.com', '$2y$10$hasta139', '05389012346', 'İnegöl/Bursa', 4),
                                                                      ('Nazmi Korkmaz', 'nazmi.korkmaz@gmail.com', '$2y$10$hasta140', '05490123457', 'Manavgat/Antalya', 4),

                                                                      ('Oktay Aydın', 'oktay.aydin@gmail.com', '$2y$10$hasta141', '05301234568', 'Fatih/İstanbul', 4),
                                                                      ('Peri Demirtaş', 'peri.demirtas@gmail.com', '$2y$10$hasta142', '05512345679', 'Sincan/Ankara', 4),
                                                                      ('Rüya Şen', 'ruya.sen@gmail.com', '$2y$10$hasta143', '05323456780', 'Konak/İzmir', 4),
                                                                      ('Sertaç Güler', 'sertac.guler@gmail.com', '$2y$10$hasta144', '05434567891', 'Nilüfer/Bursa', 4),
                                                                      ('Tülay Sarı', 'tulay.sari@gmail.com', '$2y$10$hasta145', '05345678902', 'Kepez/Antalya', 4),
                                                                      ('Uğurcan Koç', 'ugurcan.koc@gmail.com', '$2y$10$hasta146', '05456789013', 'Şişli/İstanbul', 4),
                                                                      ('Veli Yılmaz', 'veli.yilmaz@gmail.com', '$2y$10$hasta147', '05367890124', 'Etimesgut/Ankara', 4),
                                                                      ('Yıldız Can', 'yildiz.can@gmail.com', '$2y$10$hasta148', '05478901235', 'Gaziemir/İzmir', 4),
                                                                      ('Zekiye Demir', 'zekiye.demir@gmail.com', '$2y$10$hasta149', '05389012346', 'Gemlik/Bursa', 4),
                                                                      ('Adnan Kaya', 'adnan.kaya@gmail.com', '$2y$10$hasta150', '05490123457', 'Alanya/Antalya', 4),

                                                                      ('Bahar Şahin', 'bahar.sahin@gmail.com', '$2y$10$hasta151', '05301234568', 'Bakırköy/İstanbul', 4),
                                                                      ('Cengiz Arslan', 'cengiz.arslan@gmail.com', '$2y$10$hasta152', '05512345679', 'Çankaya/Ankara', 4),
                                                                      ('Dilek Öztürk', 'dilek.ozturk@gmail.com', '$2y$10$hasta153', '05323456780', 'Karşıyaka/İzmir', 4),
                                                                      ('Emir Çelik', 'emir.celik@gmail.com', '$2y$10$hasta154', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Figen Yıldız', 'figen.yildiz@gmail.com', '$2y$10$hasta155', '05345678902', 'Lara/Antalya', 4),
                                                                      ('Güven Korkmaz', 'guven.korkmaz@gmail.com', '$2y$10$hasta156', '05456789013', 'Kadıköy/İstanbul', 4),
                                                                      ('Hüseyin Aydın', 'huseyin.aydin@gmail.com', '$2y$10$hasta157', '05367890124', 'Keçiören/Ankara', 4),
                                                                      ('İrem Demirtaş', 'irem.demirtas@gmail.com', '$2y$10$hasta158', '05478901235', 'Bornova/İzmir', 4),
                                                                      ('Kazım Şen', 'kazim.sen@gmail.com', '$2y$10$hasta159', '05389012346', 'Yıldırım/Bursa', 4),
                                                                      ('Leman Güler', 'leman.guler@gmail.com', '$2y$10$hasta160', '05490123457', 'Muratpaşa/Antalya', 4),

                                                                      ('Müjde Sarı', 'mujde.sari@gmail.com', '$2y$10$hasta161', '05301234568', 'Üsküdar/İstanbul', 4),
                                                                      ('Necati Koç', 'necati.koc@gmail.com', '$2y$10$hasta162', '05512345679', 'Yenimahalle/Ankara', 4),
                                                                      ('Oya Yılmaz', 'oya.yilmaz@gmail.com', '$2y$10$hasta163', '05323456780', 'Buca/İzmir', 4),
                                                                      ('Pamir Can', 'pamir.can@gmail.com', '$2y$10$hasta164', '05434567891', 'Osmangazi/Bursa', 4),
                                                                      ('Rana Demir', 'rana.demir@gmail.com', '$2y$10$hasta165', '05345678902', 'Konyaaltı/Antalya', 4),
                                                                      ('Sami Kaya', 'sami.kaya@gmail.com', '$2y$10$hasta166', '05456789013', 'Beşiktaş/İstanbul', 4),
                                                                      ('Türkay Şahin', 'turkay.sahin@gmail.com', '$2y$10$hasta167', '05367890124', 'Mamak/Ankara', 4),
                                                                      ('Umut Arslan', 'umut.arslan@gmail.com', '$2y$10$hasta168', '05478901235', 'Alsancak/İzmir', 4),
                                                                      ('Vuslat Öztürk', 'vuslat.ozturk@gmail.com', '$2y$10$hasta169', '05389012346', 'İnegöl/Bursa', 4),
                                                                      ('Yavuz Çelik', 'yavuz.celik@gmail.com', '$2y$10$hasta170', '05490123457', 'Manavgat/Antalya', 4),

                                                                      ('Zehra Yıldız', 'zehra.yildiz@gmail.com', '$2y$10$hasta171', '05301234568', 'Fatih/İstanbul', 4),
                                                                      ('Abdullah Korkmaz', 'abdullah.korkmaz@gmail.com', '$2y$10$hasta172', '05512345679', 'Sincan/Ankara', 4),
                                                                      ('Beste Aydın', 'beste.aydin@gmail.com', '$2y$10$hasta173', '05323456780', 'Konak/İzmir', 4),
                                                                      ('Cevat Demirtaş', 'cevat.demirtas@gmail.com', '$2y$10$hasta174', '05434567891', 'Nilüfer/Bursa', 4),
                                                                      ('Duygu Şen', 'duygu.sen@gmail.com', '$2y$10$hasta175', '05345678902', 'Kepez/Antalya', 4);

-- -----------------------------------------------------
-- 3. HASTANELER (5 İl, Her İlde 5 Hastane = 25 Hastane)
-- -----------------------------------------------------
INSERT INTO Hospital (name, address, phone, city) VALUES
-- İstanbul Hastaneleri
('İstanbul Acıbadem Hastanesi', 'Tekin Sok. No:8 Acıbadem/İstanbul', '0216 555 1010', 'İstanbul'),
('İstanbul Memorial Hastanesi', 'Nispetiye Cad. No:12 Beşiktaş/İstanbul', '0212 555 1020', 'İstanbul'),
('İstanbul Medipol Hastanesi', 'Esenler Yolu No:25 Üsküdar/İstanbul', '0216 555 1030', 'İstanbul'),
('İstanbul Florence Hastanesi', 'Büyükdere Cad. No:45 Şişli/İstanbul', '0212 555 1040', 'İstanbul'),
('İstanbul Liv Hastanesi', 'Atatürk Cad. No:67 Bakırköy/İstanbul', '0212 555 1050', 'İstanbul'),

-- Ankara Hastaneleri
('Ankara Güven Hastanesi', 'Şimşek Sok. No:29 Kavaklıdere/Ankara', '0312 555 2010', 'Ankara'),
('Ankara Medicana Hastanesi', 'Söğütözü Cad. No:15 Çankaya/Ankara', '0312 555 2020', 'Ankara'),
('Ankara City Hastanesi', 'Yunus Emre Mah. No:8 Yenimahalle/Ankara', '0312 555 2030', 'Ankara'),
('Ankara Bilkent Hastanesi', 'Bilkent Üniversitesi Kampüsü/Ankara', '0312 555 2040', 'Ankara'),
('Ankara Etlik Hastanesi', 'Etlik Cad. No:33 Keçiören/Ankara', '0312 555 2050', 'Ankara'),

-- İzmir Hastaneleri
('İzmir Egepol Hastanesi', 'Yenişehir Mah. 1333 Sok. No:1 Konak/İzmir', '0232 555 3010', 'İzmir'),
('İzmir Medical Park Hastanesi', 'Şair Eşref Bulvarı No:5 Karşıyaka/İzmir', '0232 555 3020', 'İzmir'),
('İzmir Kent Hastanesi', 'Mithatpaşa Cad. No:45 Bornova/İzmir', '0232 555 3030', 'İzmir'),
('İzmir Özel Hastanesi', 'Fevzi Çakmak Cad. No:23 Buca/İzmir', '0232 555 3040', 'İzmir'),
('İzmir Alsancak Hastanesi', 'Cumhuriyet Bulvarı No:67 Alsancak/İzmir', '0232 555 3050', 'İzmir'),

-- Bursa Hastaneleri
('Bursa Acıbadem Hastanesi', 'Fethiye Mah. FSM Bulvarı No:1 Nilüfer/Bursa', '0224 555 4010', 'Bursa'),
('Bursa Doruk Hastanesi', 'Altıparmak Cad. No:15 Osmangazi/Bursa', '0224 555 4020', 'Bursa'),
('Bursa Medical Park Hastanesi', 'Ataevler Mah. No:25 Nilüfer/Bursa', '0224 555 4030', 'Bursa'),
('Bursa Çekirge Hastanesi', 'Çekirge Cad. No:33 Osmangazi/Bursa', '0224 555 4040', 'Bursa'),
('Bursa Gemlik Hastanesi', 'Cumhuriyet Mah. No:12 Gemlik/Bursa', '0224 555 4050', 'Bursa'),

-- Antalya Hastaneleri
('Antalya Yaşam Hastanesi', 'Şirinyalı Mah. İsmet Gökşen Cad. Muratpaşa/Antalya', '0242 555 5010', 'Antalya'),
('Antalya Memorial Hastanesi', 'Konyaaltı Cad. No:18 Konyaaltı/Antalya', '0242 555 5020', 'Antalya'),
('Antalya Medical Park Hastanesi', 'Fener Mah. No:25 Lara/Antalya', '0242 555 5030', 'Antalya'),
('Antalya Anadolu Hastanesi', 'Meltem Mah. No:8 Kepez/Antalya', '0242 555 5040', 'Antalya'),
('Antalya Alanya Hastanesi', 'Saray Mah. No:15 Alanya/Antalya', '0242 555 5050', 'Antalya');

-- -----------------------------------------------------
-- 4. MANAGERLAR (Her Hastaneye 1 Manager)
-- -----------------------------------------------------
INSERT INTO Manager (user_id, hospital_id, position, hire_date, salary, qualification) VALUES
-- İstanbul Hastaneleri Managerları
(3, 1, 'Genel Müdür', '2018-03-15', 45000.00, 'İstanbul Üniversitesi İşletme Yüksek Lisans'),
(4, 2, 'İnsan Kaynakları Müdürü', '2019-06-01', 38000.00, 'Boğaziçi Üniversitesi İnsan Kaynakları'),
(5, 3, 'Mali İşler Müdürü', '2020-01-10', 42000.00, 'Marmara Üniversitesi Maliye'),
(6, 4, 'Operasyon Müdürü', '2019-09-20', 40000.00, 'İTÜ İşletme Mühendisliği'),
(7, 5, 'Pazarlama Müdürü', '2021-02-15', 39000.00, 'Koç Üniversitesi İşletme'),

-- Ankara Hastaneleri Managerları
(8, 6, 'Genel Müdür', '2017-05-20', 43000.00, 'ODTÜ İşletme'),
(9, 7, 'İnsan Kaynakları Müdürü', '2020-08-12', 37000.00, 'Hacettepe Üniversitesi Psikoloji'),
(10, 8, 'Mali İşler Müdürü', '2019-11-05', 41000.00, 'Ankara Üniversitesi Maliye'),
(11, 9, 'Operasyon Müdürü', '2018-04-18', 39000.00, 'Gazi Üniversitesi İşletme'),
(12, 10, 'Pazarlama Müdürü', '2022-01-25', 38000.00, 'Başkent Üniversitesi Pazarlama'),

-- İzmir Hastaneleri Managerları
(13, 11, 'Genel Müdür', '2019-07-30', 42000.00, 'Ege Üniversitesi İşletme'),
(14, 12, 'İnsan Kaynakları Müdürü', '2021-03-14', 36000.00, 'Dokuz Eylül Üniversitesi İK'),
(15, 13, 'Mali İşler Müdürü', '2020-06-22', 40000.00, 'İzmir Ekonomi Üniversitesi Finans'),
(16, 14, 'Operasyon Müdürü', '2018-09-08', 38000.00, 'Yaşar Üniversitesi İşletme'),
(17, 15, 'Pazarlama Müdürü', '2022-02-10', 37000.00, 'İzmir Katip Çelebi Üniversitesi Pazarlama'),

-- Bursa Hastaneleri Managerları
(18, 16, 'Genel Müdür', '2018-12-05', 41000.00, 'Uludağ Üniversitesi İşletme'),
(19, 17, 'İnsan Kaynakları Müdürü', '2020-05-19', 35000.00, 'Bursa Teknik Üniversitesi İK'),
(20, 18, 'Mali İşler Müdürü', '2019-08-25', 39000.00, 'Bursa Uludağ Üniversitesi Maliye'),
(21, 19, 'Operasyon Müdürü', '2021-01-12', 37000.00, 'Bursa Orhangazi Üniversitesi İşletme'),
(22, 20, 'Pazarlama Müdürü', '2022-03-30', 36000.00, 'Bursa Teknik Üniversitesi Pazarlama'),

-- Antalya Hastaneleri Managerları
(23, 21, 'Genel Müdür', '2019-04-15', 42000.00, 'Akdeniz Üniversitesi İşletme'),
(24, 22, 'İnsan Kaynakları Müdürü', '2021-07-08', 36000.00, 'Antalya Bilim Üniversitesi İK'),
(25, 23, 'Mali İşler Müdürü', '2020-10-20', 40000.00, 'Alanya Alaaddin Keykubat Üniversitesi Finans'),
(26, 24, 'Operasyon Müdürü', '2018-11-28', 38000.00, 'Antalya Akev Üniversitesi İşletme'),
(27, 25, 'Pazarlama Müdürü', '2022-04-05', 37000.00, 'Akdeniz Üniversitesi Pazarlama');

-- -----------------------------------------------------
-- 5. KLİNİKLER VE UZMANLIKLAR
-- -----------------------------------------------------
INSERT INTO Clinic (clinic_name, location, phone) VALUES
                                                      ('Kardiyoloji Kliniği', 'A Blok 2. Kat', '0212 555 0101'),
                                                      ('Ortopedi Kliniği', 'B Blok 1. Kat', '0212 555 0102'),
                                                      ('Nöroloji Kliniği', 'A Blok 3. Kat', '0212 555 0103'),
                                                      ('Pediatri Kliniği', 'C Blok Zemin Kat', '0212 555 0104'),
                                                      ('Göz Hastalıkları Kliniği', 'B Blok 2. Kat', '0212 555 0105'),
                                                      ('Dermatoloji Kliniği', 'A Blok 1. Kat', '0212 555 0106'),
                                                      ('Psikiyatri Kliniği', 'D Blok 3. Kat', '0212 555 0107');

INSERT INTO Specialty (name, description) VALUES
                                              ('Kardiyoloji', 'Kalp ve damar hastalıkları uzmanı'),
                                              ('Ortopedi ve Travmatoloji', 'Kemik, eklem ve kas hastalıkları uzmanı'),
                                              ('Nöroloji', 'Sinir sistemi hastalıkları uzmanı'),
                                              ('Pediatri', 'Çocuk hastalıkları uzmanı'),
                                              ('Göz Hastalıkları', 'Göz sağlığı ve hastalıkları uzmanı'),
                                              ('Dermatoloji', 'Cilt hastalıkları uzmanı'),
                                              ('Psikiyatri', 'Ruh sağlığı ve hastalıkları uzmanı');

-- -----------------------------------------------------
-- 6. DOKTORLAR (175 Doktor: 7 doktor x 25 hastane)
-- -----------------------------------------------------
INSERT INTO Doctor (user_id, specialty_id, clinic_id, hospital_id, license_no, experience, education, consultation_fee) VALUES
-- İstanbul 1. Hastane (7 Doktor)
(28, 1, 1, 1, 'IST-KARD-001', 10, 'İstanbul Tıp Fakültesi', 800),
(29, 2, 2, 1, 'IST-ORTO-001', 8, 'İstanbul Tıp Fakültesi', 750),
(30, 3, 3, 1, 'IST-NORO-001', 12, 'İstanbul Tıp Fakültesi', 700),
(31, 4, 4, 1, 'IST-PED-001', 5, 'İstanbul Tıp Fakültesi', 650),
(32, 5, 5, 1, 'IST-GOZ-001', 15, 'İstanbul Tıp Fakültesi', 720),
(33, 6, 6, 1, 'IST-DERM-001', 7, 'İstanbul Tıp Fakültesi', 680),
(34, 7, 7, 1, 'IST-PSIK-001', 9, 'İstanbul Tıp Fakültesi', 700),

-- İstanbul 2. Hastane (7 Doktor)
(35, 1, 1, 2, 'IST-KARD-002', 11, 'Marmara Tıp Fakültesi', 820),
(36, 2, 2, 2, 'IST-ORTO-002', 9, 'Marmara Tıp Fakültesi', 770),
(37, 3, 3, 2, 'IST-NORO-002', 13, 'Marmara Tıp Fakültesi', 720),
(38, 4, 4, 2, 'IST-PED-002', 6, 'Marmara Tıp Fakültesi', 670),
(39, 5, 5, 2, 'IST-GOZ-002', 16, 'Marmara Tıp Fakültesi', 740),
(40, 6, 6, 2, 'IST-DERM-002', 8, 'Marmara Tıp Fakültesi', 690),
(41, 7, 7, 2, 'IST-PSIK-002', 10, 'Marmara Tıp Fakültesi', 710),

-- İstanbul 3. Hastane (7 Doktor)
(42, 1, 1, 3, 'IST-KARD-003', 12, 'Cerrahpaşa Tıp Fakültesi', 850),
(43, 2, 2, 3, 'IST-ORTO-003', 10, 'Cerrahpaşa Tıp Fakültesi', 780),
(44, 3, 3, 3, 'IST-NORO-003', 14, 'Cerrahpaşa Tıp Fakültesi', 730),
(45, 4, 4, 3, 'IST-PED-003', 7, 'Cerrahpaşa Tıp Fakültesi', 680),
(46, 5, 5, 3, 'IST-GOZ-003', 17, 'Cerrahpaşa Tıp Fakültesi', 760),
(47, 6, 6, 3, 'IST-DERM-003', 9, 'Cerrahpaşa Tıp Fakültesi', 700),
(48, 7, 7, 3, 'IST-PSIK-003', 11, 'Cerrahpaşa Tıp Fakültesi', 720),

-- İstanbul 4. Hastane (7 Doktor)
(49, 1, 1, 4, 'IST-KARD-004', 9, 'İstanbul Medipol Tıp', 780),
(50, 2, 2, 4, 'IST-ORTO-004', 7, 'İstanbul Medipol Tıp', 730),
(51, 3, 3, 4, 'IST-NORO-004', 11, 'İstanbul Medipol Tıp', 680),
(52, 4, 4, 4, 'IST-PED-004', 4, 'İstanbul Medipol Tıp', 630),
(53, 5, 5, 4, 'IST-GOZ-004', 14, 'İstanbul Medipol Tıp', 700),
(54, 6, 6, 4, 'IST-DERM-004', 6, 'İstanbul Medipol Tıp', 660),
(55, 7, 7, 4, 'IST-PSIK-004', 8, 'İstanbul Medipol Tıp', 680),

-- İstanbul 5. Hastane (7 Doktor)
(56, 1, 1, 5, 'IST-KARD-005', 13, 'Acıbadem Tıp Fakültesi', 870),
(57, 2, 2, 5, 'IST-ORTO-005', 11, 'Acıbadem Tıp Fakültesi', 800),
(58, 3, 3, 5, 'IST-NORO-005', 15, 'Acıbadem Tıp Fakültesi', 750),
(59, 4, 4, 5, 'IST-PED-005', 8, 'Acıbadem Tıp Fakültesi', 700),
(60, 5, 5, 5, 'IST-GOZ-005', 18, 'Acıbadem Tıp Fakültesi', 780),
(61, 6, 6, 5, 'IST-DERM-005', 10, 'Acıbadem Tıp Fakültesi', 720),
(62, 7, 7, 5, 'IST-PSIK-005', 12, 'Acıbadem Tıp Fakültesi', 740),

-- Ankara 1. Hastane (7 Doktor)
(63, 1, 1, 6, 'ANK-KARD-001', 10, 'Hacettepe Tıp Fakültesi', 750),
(64, 2, 2, 6, 'ANK-ORTO-001', 8, 'Hacettepe Tıp Fakültesi', 700),
(65, 3, 3, 6, 'ANK-NORO-001', 12, 'Hacettepe Tıp Fakültesi', 680),
(66, 4, 4, 6, 'ANK-PED-001', 5, 'Hacettepe Tıp Fakültesi', 620),
(67, 5, 5, 6, 'ANK-GOZ-001', 15, 'Hacettepe Tıp Fakültesi', 700),
(68, 6, 6, 6, 'ANK-DERM-001', 7, 'Hacettepe Tıp Fakültesi', 650),
(69, 7, 7, 6, 'ANK-PSIK-001', 9, 'Hacettepe Tıp Fakültesi', 670),

-- Ankara 2. Hastane (7 Doktor)
(70, 1, 1, 7, 'ANK-KARD-002', 11, 'Ankara Tıp Fakültesi', 770),
(71, 2, 2, 7, 'ANK-ORTO-002', 9, 'Ankara Tıp Fakültesi', 720),
(72, 3, 3, 7, 'ANK-NORO-002', 13, 'Ankara Tıp Fakültesi', 690),
(73, 4, 4, 7, 'ANK-PED-002', 6, 'Ankara Tıp Fakültesi', 630),
(74, 5, 5, 7, 'ANK-GOZ-002', 16, 'Ankara Tıp Fakültesi', 710),
(75, 6, 6, 7, 'ANK-DERM-002', 8, 'Ankara Tıp Fakültesi', 660),
(76, 7, 7, 7, 'ANK-PSIK-002', 10, 'Ankara Tıp Fakültesi', 680),

-- Ankara 3. Hastane (7 Doktor)
(77, 1, 1, 8, 'ANK-KARD-003', 12, 'Gazi Tıp Fakültesi', 790),
(78, 2, 2, 8, 'ANK-ORTO-003', 10, 'Gazi Tıp Fakültesi', 740),
(79, 3, 3, 8, 'ANK-NORO-003', 14, 'Gazi Tıp Fakültesi', 700),
(80, 4, 4, 8, 'ANK-PED-003', 7, 'Gazi Tıp Fakültesi', 640),
(81, 5, 5, 8, 'ANK-GOZ-003', 17, 'Gazi Tıp Fakültesi', 720),
(82, 6, 6, 8, 'ANK-DERM-003', 9, 'Gazi Tıp Fakültesi', 670),
(83, 7, 7, 8, 'ANK-PSIK-003', 11, 'Gazi Tıp Fakültesi', 690),

-- Ankara 4. Hastane (7 Doktor)
(84, 1, 1, 9, 'ANK-KARD-004', 9, 'Ankara Medipol Tıp', 730),
(85, 2, 2, 9, 'ANK-ORTO-004', 7, 'Ankara Medipol Tıp', 680),
(86, 3, 3, 9, 'ANK-NORO-004', 11, 'Ankara Medipol Tıp', 650),
(87, 4, 4, 9, 'ANK-PED-004', 4, 'Ankara Medipol Tıp', 600),
(88, 5, 5, 9, 'ANK-GOZ-004', 14, 'Ankara Medipol Tıp', 680),
(89, 6, 6, 9, 'ANK-DERM-004', 6, 'Ankara Medipol Tıp', 630),
(90, 7, 7, 9, 'ANK-PSIK-004', 8, 'Ankara Medipol Tıp', 650),

-- Ankara 5. Hastane (7 Doktor)
(91, 1, 1, 10, 'ANK-KARD-005', 13, 'Ankara Memorial Tıp', 810),
(92, 2, 2, 10, 'ANK-ORTO-005', 11, 'Ankara Memorial Tıp', 760),
(93, 3, 3, 10, 'ANK-NORO-005', 15, 'Ankara Memorial Tıp', 720),
(94, 4, 4, 10, 'ANK-PED-005', 8, 'Ankara Memorial Tıp', 660),
(95, 5, 5, 10, 'ANK-GOZ-005', 18, 'Ankara Memorial Tıp', 740),
(96, 6, 6, 10, 'ANK-DERM-005', 10, 'Ankara Memorial Tıp', 690),
(97, 7, 7, 10, 'ANK-PSIK-005', 12, 'Ankara Memorial Tıp', 710),

-- İzmir 1. Hastane (7 Doktor)
(98, 1, 1, 11, 'IZM-KARD-001', 10, 'Ege Tıp Fakültesi', 720),
(99, 2, 2, 11, 'IZM-ORTO-001', 8, 'Ege Tıp Fakültesi', 680),
(100, 3, 3, 11, 'IZM-NORO-001', 12, 'Ege Tıp Fakültesi', 650),
(101, 4, 4, 11, 'IZM-PED-001', 5, 'Ege Tıp Fakültesi', 600),
(102, 5, 5, 11, 'IZM-GOZ-001', 15, 'Ege Tıp Fakültesi', 680),
(103, 6, 6, 11, 'IZM-DERM-001', 7, 'Ege Tıp Fakültesi', 630),
(104, 7, 7, 11, 'IZM-PSIK-001', 9, 'Ege Tıp Fakültesi', 650),

-- İzmir 2. Hastane (7 Doktor)
(105, 1, 1, 12, 'IZM-KARD-002', 11, 'Dokuz Eylül Tıp Fakültesi', 740),
(106, 2, 2, 12, 'IZM-ORTO-002', 9, 'Dokuz Eylül Tıp Fakültesi', 690),
(107, 3, 3, 12, 'IZM-NORO-002', 13, 'Dokuz Eylül Tıp Fakültesi', 660),
(108, 4, 4, 12, 'IZM-PED-002', 6, 'Dokuz Eylül Tıp Fakültesi', 610),
(109, 5, 5, 12, 'IZM-GOZ-002', 16, 'Dokuz Eylül Tıp Fakültesi', 690),
(110, 6, 6, 12, 'IZM-DERM-002', 8, 'Dokuz Eylül Tıp Fakültesi', 640),
(111, 7, 7, 12, 'IZM-PSIK-002', 10, 'Dokuz Eylül Tıp Fakültesi', 660),

-- İzmir 3. Hastane (7 Doktor)
(112, 1, 1, 13, 'IZM-KARD-003', 12, 'İzmir Katip Çelebi Tıp', 760),
(113, 2, 2, 13, 'IZM-ORTO-003', 10, 'İzmir Katip Çelebi Tıp', 710),
(114, 3, 3, 13, 'IZM-NORO-003', 14, 'İzmir Katip Çelebi Tıp', 680),
(115, 4, 4, 13, 'IZM-PED-003', 7, 'İzmir Katip Çelebi Tıp', 620),
(116, 5, 5, 13, 'IZM-GOZ-003', 17, 'İzmir Katip Çelebi Tıp', 700),
(117, 6, 6, 13, 'IZM-DERM-003', 9, 'İzmir Katip Çelebi Tıp', 650),
(118, 7, 7, 13, 'IZM-PSIK-003', 11, 'İzmir Katip Çelebi Tıp', 670),

-- İzmir 4. Hastane (7 Doktor)
(119, 1, 1, 14, 'IZM-KARD-004', 9, 'İzmir Ekonomi Tıp', 700),
(120, 2, 2, 14, 'IZM-ORTO-004', 7, 'İzmir Ekonomi Tıp', 660),
(121, 3, 3, 14, 'IZM-NORO-004', 11, 'İzmir Ekonomi Tıp', 630),
(122, 4, 4, 14, 'IZM-PED-004', 4, 'İzmir Ekonomi Tıp', 580),
(123, 5, 5, 14, 'IZM-GOZ-004', 14, 'İzmir Ekonomi Tıp', 660),
(124, 6, 6, 14, 'IZM-DERM-004', 6, 'İzmir Ekonomi Tıp', 610),
(125, 7, 7, 14, 'IZM-PSIK-004', 8, 'İzmir Ekonomi Tıp', 630),

-- İzmir 5. Hastane (7 Doktor)
(126, 1, 1, 15, 'IZM-KARD-005', 13, 'İzmir Medical Park Tıp', 780),
(127, 2, 2, 15, 'IZM-ORTO-005', 11, 'İzmir Medical Park Tıp', 730),
(128, 3, 3, 15, 'IZM-NORO-005', 15, 'İzmir Medical Park Tıp', 700),
(129, 4, 4, 15, 'IZM-PED-005', 8, 'İzmir Medical Park Tıp', 640),
(130, 5, 5, 15, 'IZM-GOZ-005', 18, 'İzmir Medical Park Tıp', 720),
(131, 6, 6, 15, 'IZM-DERM-005', 10, 'İzmir Medical Park Tıp', 670),
(132, 7, 7, 15, 'IZM-PSIK-005', 12, 'İzmir Medical Park Tıp', 690),

-- Bursa 1. Hastane (7 Doktor)
(133, 1, 1, 16, 'BRS-KARD-001', 10, 'Uludağ Tıp Fakültesi', 700),
(134, 2, 2, 16, 'BRS-ORTO-001', 8, 'Uludağ Tıp Fakültesi', 660),
(135, 3, 3, 16, 'BRS-NORO-001', 12, 'Uludağ Tıp Fakültesi', 630),
(136, 4, 4, 16, 'BRS-PED-001', 5, 'Uludağ Tıp Fakültesi', 580),
(137, 5, 5, 16, 'BRS-GOZ-001', 15, 'Uludağ Tıp Fakültesi', 660),
(138, 6, 6, 16, 'BRS-DERM-001', 7, 'Uludağ Tıp Fakültesi', 610),
(139, 7, 7, 16, 'BRS-PSIK-001', 9, 'Uludağ Tıp Fakültesi', 630),

-- Bursa 2. Hastane (7 Doktor)
(140, 1, 1, 17, 'BRS-KARD-002', 11, 'Bursa Medical Park Tıp', 720),
(141, 2, 2, 17, 'BRS-ORTO-002', 9, 'Bursa Medical Park Tıp', 670),
(142, 3, 3, 17, 'BRS-NORO-002', 13, 'Bursa Medical Park Tıp', 640),
(143, 4, 4, 17, 'BRS-PED-002', 6, 'Bursa Medical Park Tıp', 590),
(144, 5, 5, 17, 'BRS-GOZ-002', 16, 'Bursa Medical Park Tıp', 670),
(145, 6, 6, 17, 'BRS-DERM-002', 8, 'Bursa Medical Park Tıp', 620),
(146, 7, 7, 17, 'BRS-PSIK-002', 10, 'Bursa Medical Park Tıp', 640),

-- Bursa 3. Hastane (7 Doktor)
(147, 1, 1, 18, 'BRS-KARD-003', 12, 'Bursa Acıbadem Tıp', 740),
(148, 2, 2, 18, 'BRS-ORTO-003', 10, 'Bursa Acıbadem Tıp', 690),
(149, 3, 3, 18, 'BRS-NORO-003', 14, 'Bursa Acıbadem Tıp', 660),
(150, 4, 4, 18, 'BRS-PED-003', 7, 'Bursa Acıbadem Tıp', 600),
(151, 5, 5, 18, 'BRS-GOZ-003', 17, 'Bursa Acıbadem Tıp', 680),
(152, 6, 6, 18, 'BRS-DERM-003', 9, 'Bursa Acıbadem Tıp', 630),
(153, 7, 7, 18, 'BRS-PSIK-003', 11, 'Bursa Acıbadem Tıp', 650),

-- Bursa 4. Hastane (7 Doktor)
(154, 1, 1, 19, 'BRS-KARD-004', 9, 'Bursa Doruk Tıp', 680),
(155, 2, 2, 19, 'BRS-ORTO-004', 7, 'Bursa Doruk Tıp', 640),
(156, 3, 3, 19, 'BRS-NORO-004', 11, 'Bursa Doruk Tıp', 610),
(157, 4, 4, 19, 'BRS-PED-004', 4, 'Bursa Doruk Tıp', 560),
(158, 5, 5, 19, 'BRS-GOZ-004', 14, 'Bursa Doruk Tıp', 640),
(159, 6, 6, 19, 'BRS-DERM-004', 6, 'Bursa Doruk Tıp', 590),
(160, 7, 7, 19, 'BRS-PSIK-004', 8, 'Bursa Doruk Tıp', 610),

-- Bursa 5. Hastane (7 Doktor)
(161, 1, 1, 20, 'BRS-KARD-005', 13, 'Bursa Çekirge Tıp', 760),
(162, 2, 2, 20, 'BRS-ORTO-005', 11, 'Bursa Çekirge Tıp', 710),
(163, 3, 3, 20, 'BRS-NORO-005', 15, 'Bursa Çekirge Tıp', 680),
(164, 4, 4, 20, 'BRS-PED-005', 8, 'Bursa Çekirge Tıp', 620),
(165, 5, 5, 20, 'BRS-GOZ-005', 18, 'Bursa Çekirge Tıp', 700),
(166, 6, 6, 20, 'BRS-DERM-005', 10, 'Bursa Çekirge Tıp', 650),
(167, 7, 7, 20, 'BRS-PSIK-005', 12, 'Bursa Çekirge Tıp', 670),

-- Antalya 1. Hastane (7 Doktor)
(168, 1, 1, 21, 'ANT-KARD-001', 10, 'Akdeniz Tıp Fakültesi', 720),
(169, 2, 2, 21, 'ANT-ORTO-001', 8, 'Akdeniz Tıp Fakültesi', 680),
(170, 3, 3, 21, 'ANT-NORO-001', 12, 'Akdeniz Tıp Fakültesi', 650),
(171, 4, 4, 21, 'ANT-PED-001', 5, 'Akdeniz Tıp Fakültesi', 600),
(172, 5, 5, 21, 'ANT-GOZ-001', 15, 'Akdeniz Tıp Fakültesi', 680),
(173, 6, 6, 21, 'ANT-DERM-001', 7, 'Akdeniz Tıp Fakültesi', 630),
(174, 7, 7, 21, 'ANT-PSIK-001', 9, 'Akdeniz Tıp Fakültesi', 650),

-- Antalya 2. Hastane (7 Doktor)
(175, 1, 1, 22, 'ANT-KARD-002', 11, 'Antalya Memorial Tıp', 740),
(176, 2, 2, 22, 'ANT-ORTO-002', 9, 'Antalya Memorial Tıp', 690),
(177, 3, 3, 22, 'ANT-NORO-002', 13, 'Antalya Memorial Tıp', 660),
(178, 4, 4, 22, 'ANT-PED-002', 6, 'Antalya Memorial Tıp', 610),
(179, 5, 5, 22, 'ANT-GOZ-002', 16, 'Antalya Memorial Tıp', 690),
(180, 6, 6, 22, 'ANT-DERM-002', 8, 'Antalya Memorial Tıp', 640),
(181, 7, 7, 22, 'ANT-PSIK-002', 10, 'Antalya Memorial Tıp', 660),

-- Antalya 3. Hastane (7 Doktor)
(182, 1, 1, 23, 'ANT-KARD-003', 12, 'Antalya Medical Park Tıp', 760),
(183, 2, 2, 23, 'ANT-ORTO-003', 10, 'Antalya Medical Park Tıp', 710),
(184, 3, 3, 23, 'ANT-NORO-003', 14, 'Antalya Medical Park Tıp', 680),
(185, 4, 4, 23, 'ANT-PED-003', 7, 'Antalya Medical Park Tıp', 620),
(186, 5, 5, 23, 'ANT-GOZ-003', 17, 'Antalya Medical Park Tıp', 700),
(187, 6, 6, 23, 'ANT-DERM-003', 9, 'Antalya Medical Park Tıp', 650),
(188, 7, 7, 23, 'ANT-PSIK-003', 11, 'Antalya Medical Park Tıp', 670),

-- Antalya 4. Hastane (7 Doktor)
(189, 1, 1, 24, 'ANT-KARD-004', 9, 'Antalya Anadolu Tıp', 700),
(190, 2, 2, 24, 'ANT-ORTO-004', 7, 'Antalya Anadolu Tıp', 660),
(191, 3, 3, 24, 'ANT-NORO-004', 11, 'Antalya Anadolu Tıp', 630),
(192, 4, 4, 24, 'ANT-PED-004', 4, 'Antalya Anadolu Tıp', 580),
(193, 5, 5, 24, 'ANT-GOZ-004', 14, 'Antalya Anadolu Tıp', 660),
(194, 6, 6, 24, 'ANT-DERM-004', 6, 'Antalya Anadolu Tıp', 610),
(195, 7, 7, 24, 'ANT-PSIK-004', 8, 'Antalya Anadolu Tıp', 630),

-- Antalya 5. Hastane (7 Doktor)
(196, 1, 1, 25, 'ANT-KARD-005', 13, 'Antalya Alanya Tıp', 780),
(197, 2, 2, 25, 'ANT-ORTO-005', 11, 'Antalya Alanya Tıp', 730),
(198, 3, 3, 25, 'ANT-NORO-005', 15, 'Antalya Alanya Tıp', 700),
(199, 4, 4, 25, 'ANT-PED-005', 8, 'Antalya Alanya Tıp', 640),
(200, 5, 5, 25, 'ANT-GOZ-005', 18, 'Antalya Alanya Tıp', 720),
(201, 6, 6, 25, 'ANT-DERM-005', 10, 'Antalya Alanya Tıp', 670),
(202, 7, 7, 25, 'ANT-PSIK-005', 12, 'Antalya Alanya Tıp', 690);

-- -----------------------------------------------------
-- 7. HASTALAR (175 Hasta)
-- -----------------------------------------------------
INSERT INTO Patient (user_id, blood_type, date_of_birth, insurance_no, emergency_contact) VALUES
                                                                                              (203, 'A+', '1985-05-20', 'SGK-1234567890', 'Emel Taş - 05322345679'),
                                                                                              (204, 'B+', '1992-08-15', 'SGK-1234567891', 'Can Çakır - 05433456788'),
                                                                                              (205, 'O+', '1988-03-10', 'SGK-1234567892', 'Ayşe Yalçın - 05344567889'),
                                                                                              (206, 'AB+', '1995-11-25', 'SGK-1234567893', 'Mehmet Kara - 05445678900'),
                                                                                              (207, 'A-', '1980-07-08', 'SGK-1234567894', 'Fatma Tekin - 05356789011'),
                                                                                              (208, 'O+', '1998-02-14', 'SGK-1234567895', 'Ali Güneş - 05457890122'),
                                                                                              (209, 'B+', '1987-09-30', 'SGK-1234567896', 'Zeynep Ateş - 05368901233'),
                                                                                              (210, 'A+', '1993-12-05', 'SGK-1234567897', 'Ahmet Vural - 05469012344'),
                                                                                              (211, 'AB-', '1990-04-18', 'SGK-1234567898', 'Elif Özkan - 05370123455'),
                                                                                              (212, 'O-', '1986-06-22', 'SGK-1234567899', 'Mustafa Şen - 05471234566'),

                                                                                              (213, 'A+', '1991-01-30', 'SGK-1234567900', 'Merve Acar - 05382345677'),
                                                                                              (214, 'B-', '1984-07-12', 'SGK-1234567901', 'Kemal Öz - 05483456788'),
                                                                                              (215, 'O+', '1996-03-25', 'SGK-1234567902', 'Sibel Demir - 05394567899'),
                                                                                              (216, 'AB+', '1989-09-08', 'SGK-1234567903', 'Hakan Yurt - 05495678900'),
                                                                                              (217, 'A+', '1994-11-15', 'SGK-1234567904', 'Deniz Can - 05306789011'),
                                                                                              (218, 'B+', '1983-04-20', 'SGK-1234567905', 'Cemre Bulut - 05507890122'),
                                                                                              (219, 'O-', '1997-08-05', 'SGK-1234567906', 'Burak Eren - 05318901233'),
                                                                                              (220, 'A-', '1982-12-18', 'SGK-1234567907', 'Aylin Kaya - 05419012344'),
                                                                                              (221, 'AB+', '1999-06-30', 'SGK-1234567908', 'Serkan Koç - 05320123455'),
                                                                                              (222, 'O+', '1981-02-14', 'SGK-1234567909', 'Gamze Arslan - 05421234566'),

                                                                                              (223, 'B+', '1988-10-22', 'SGK-1234567910', 'Hüseyin Öztürk - 05331234567'),
                                                                                              (224, 'A+', '1993-05-07', 'SGK-1234567911', 'Nazan Yılmaz - 05442345678'),
                                                                                              (225, 'O+', '1986-11-11', 'SGK-1234567912', 'Orhan Şahin - 05353456789'),
                                                                                              (226, 'AB-', '1995-03-28', 'SGK-1234567913', 'Perihan Polat - 05454567890'),
                                                                                              (227, 'A+', '1990-07-03', 'SGK-1234567914', 'Rıza Aydın - 05365678901'),
                                                                                              (228, 'B+', '1987-12-16', 'SGK-1234567915', 'Selma Kurt - 05466789012'),
                                                                                              (229, 'O-', '1994-09-09', 'SGK-1234567916', 'Taner Tekin - 05377890123'),
                                                                                              (230, 'A+', '1989-04-24', 'SGK-1234567917', 'Uğur Yıldız - 05478901234'),
                                                                                              (231, 'AB+', '1992-01-17', 'SGK-1234567918', 'Vildan Aksoy - 05389012345'),
                                                                                              (232, 'O+', '1985-08-02', 'SGK-1234567919', 'Yusuf Çelik - 05490123456'),

                                                                                              (233, 'A-', '1991-06-13', 'SGK-1234567920', 'Zehra Özer - 05301234567'),
                                                                                              (234, 'B+', '1988-03-26', 'SGK-1234567921', 'Ahmet Şimşek - 05512345678'),
                                                                                              (235, 'O+', '1996-10-19', 'SGK-1234567922', 'Ayşe Güler - 05323456789'),
                                                                                              (236, 'AB+', '1984-05-04', 'SGK-1234567923', 'Mehmet Avcı - 05434567890'),
                                                                                              (237, 'A+', '1993-12-27', 'SGK-1234567924', 'Fatma Konak - 05345678901'),
                                                                                              (238, 'B-', '1990-02-08', 'SGK-1234567925', 'Ali Yılmaz - 05456789012'),
                                                                                              (239, 'O+', '1987-07-21', 'SGK-1234567926', 'Zeynep Şahin - 05367890123'),
                                                                                              (240, 'A+', '1995-04-14', 'SGK-1234567927', 'Mustafa Demir - 05478901234'),
                                                                                              (241, 'AB+', '1989-11-07', 'SGK-1234567928', 'Elif Kaya - 05389012345'),
                                                                                              (242, 'O-', '1992-08-30', 'SGK-1234567929', 'Kemal Arslan - 05490123456'),

                                                                                              (243, 'B+', '1986-01-23', 'SGK-1234567930', 'Merve Öztürk - 05301234567'),
                                                                                              (244, 'A+', '1994-06-16', 'SGK-1234567931', 'Can Çelik - 05512345678'),
                                                                                              (245, 'O+', '1988-09-29', 'SGK-1234567932', 'Seda Yıldız - 05323456789'),
                                                                                              (246, 'AB-', '1991-03-12', 'SGK-1234567933', 'Burak Korkmaz - 05434567890'),
                                                                                              (247, 'A+', '1997-10-05', 'SGK-1234567934', 'Deniz Aydın - 05345678901'),
                                                                                              (248, 'B+', '1985-05-18', 'SGK-1234567935', 'Cemre Demirtaş - 05456789012'),
                                                                                              (249, 'O+', '1993-12-01', 'SGK-1234567936', 'Eren Şen - 05367890123'),
                                                                                              (250, 'A-', '1990-07-24', 'SGK-1234567937', 'Aylin Güler - 05478901234'),
                                                                                              (251, 'AB+', '1987-02-17', 'SGK-1234567938', 'Serkan Sarı - 05389012345'),
                                                                                              (252, 'O+', '1995-11-10', 'SGK-1234567939', 'Gamze Koç - 05490123456'),

                                                                                              (253, 'A+', '1989-04-03', 'SGK-1234567940', 'Hakan Yılmaz - 05301234567'),
                                                                                              (254, 'B+', '1992-08-26', 'SGK-1234567941', 'Nazan Can - 05512345678'),
                                                                                              (255, 'O-', '1986-01-19', 'SGK-1234567942', 'Orhan Demir - 05323456789'),
                                                                                              (256, 'AB+', '1994-06-12', 'SGK-1234567943', 'Perihan Kaya - 05434567890'),
                                                                                              (257, 'A+', '1988-11-05', 'SGK-1234567944', 'Rıza Şahin - 05345678901'),
                                                                                              (258, 'B+', '1991-03-28', 'SGK-1234567945', 'Selma Arslan - 05456789012'),
                                                                                              (259, 'O+', '1996-10-21', 'SGK-1234567946', 'Taner Öztürk - 05367890123'),
                                                                                              (260, 'A+', '1984-05-14', 'SGK-1234567947', 'Uğur Çelik - 05478901234'),
                                                                                              (261, 'AB-', '1993-12-07', 'SGK-1234567948', 'Vildan Yıldız - 05389012345'),
                                                                                              (262, 'O+', '1990-07-30', 'SGK-1234567949', 'Yusuf Korkmaz - 05490123456'),

                                                                                              (263, 'B+', '1987-02-22', 'SGK-1234567950', 'Zehra Aydın - 05301234567'),
                                                                                              (264, 'A+', '1995-09-15', 'SGK-1234567951', 'Ahmet Demirtaş - 05512345678'),
                                                                                              (265, 'O+', '1989-04-08', 'SGK-1234567952', 'Ayşe Şen - 05323456789'),
                                                                                              (266, 'AB+', '1992-11-01', 'SGK-1234567953', 'Mehmet Güler - 05434567890'),
                                                                                              (267, 'A-', '1986-06-24', 'SGK-1234567954', 'Fatma Sarı - 05345678901'),
                                                                                              (268, 'B+', '1994-01-17', 'SGK-1234567955', 'Ali Koç - 05456789012'),
                                                                                              (269, 'O+', '1988-08-10', 'SGK-1234567956', 'Zeynep Yılmaz - 05367890123'),
                                                                                              (270, 'A+', '1991-03-03', 'SGK-1234567957', 'Mustafa Can - 05478901234'),
                                                                                              (271, 'AB+', '1997-10-26', 'SGK-1234567958', 'Elif Demir - 05389012345'),
                                                                                              (272, 'O-', '1985-05-19', 'SGK-1234567959', 'Kemal Kaya - 05490123456'),

                                                                                              (273, 'A+', '1993-12-12', 'SGK-1234567960', 'Merve Şahin - 05301234567'),
                                                                                              (274, 'B+', '1990-07-05', 'SGK-1234567961', 'Can Arslan - 05512345678'),
                                                                                              (275, 'O+', '1987-02-28', 'SGK-1234567962', 'Seda Öztürk - 05323456789'),
                                                                                              (276, 'AB+', '1995-09-21', 'SGK-1234567963', 'Burak Çelik - 05434567890'),
                                                                                              (277, 'A+', '1989-04-14', 'SGK-1234567964', 'Deniz Yıldız - 05345678901'),
                                                                                              (278, 'B-', '1992-11-07', 'SGK-1234567965', 'Cemre Korkmaz - 05456789012'),
                                                                                              (279, 'O+', '1986-06-30', 'SGK-1234567966', 'Eren Aydın - 05367890123'),
                                                                                              (280, 'A+', '1994-01-23', 'SGK-1234567967', 'Aylin Demirtaş - 05478901234'),
                                                                                              (281, 'AB+', '1988-08-16', 'SGK-1234567968', 'Serkan Şen - 05389012345'),
                                                                                              (282, 'O+', '1991-03-09', 'SGK-1234567969', 'Gamze Güler - 05490123456'),

                                                                                              (283, 'B+', '1996-10-02', 'SGK-1234567970', 'Hakan Sarı - 05301234567'),
                                                                                              (284, 'A+', '1984-05-25', 'SGK-1234567971', 'Nazan Koç - 05512345678'),
                                                                                              (285, 'O-', '1993-12-18', 'SGK-1234567972', 'Orhan Yılmaz - 05323456789'),
                                                                                              (286, 'AB+', '1990-07-11', 'SGK-1234567973', 'Perihan Can - 05434567890'),
                                                                                              (287, 'A+', '1987-02-04', 'SGK-1234567974', 'Rıza Demir - 05345678901'),
                                                                                              (288, 'B+', '1995-09-27', 'SGK-1234567975', 'Selma Kaya - 05456789012'),
                                                                                              (289, 'O+', '1989-04-20', 'SGK-1234567976', 'Taner Şahin - 05367890123'),
                                                                                              (290, 'A+', '1992-11-13', 'SGK-1234567977', 'Uğur Arslan - 05478901234'),
                                                                                              (291, 'AB-', '1986-06-06', 'SGK-1234567978', 'Vildan Öztürk - 05389012345'),
                                                                                              (292, 'O+', '1994-01-29', 'SGK-1234567979', 'Yusuf Çelik - 05490123456'),

                                                                                              (293, 'A+', '1988-08-22', 'SGK-1234567980', 'Zehra Yıldız - 05301234567'),
                                                                                              (294, 'B+', '1991-03-15', 'SGK-1234567981', 'Ahmet Korkmaz - 05512345678'),
                                                                                              (295, 'O+', '1997-10-08', 'SGK-1234567982', 'Ayşe Aydın - 05323456789'),
                                                                                              (296, 'AB+', '1985-05-01', 'SGK-1234567983', 'Mehmet Demirtaş - 05434567890'),
                                                                                              (297, 'A-', '1993-12-24', 'SGK-1234567984', 'Fatma Şen - 05345678901'),
                                                                                              (298, 'B+', '1990-07-17', 'SGK-1234567985', 'Ali Güler - 05456789012'),
                                                                                              (299, 'O+', '1987-02-10', 'SGK-1234567986', 'Zeynep Sarı - 05367890123'),
                                                                                              (300, 'A+', '1995-09-03', 'SGK-1234567987', 'Mustafa Koç - 05478901234'),
                                                                                              (301, 'AB+', '1989-04-26', 'SGK-1234567988', 'Elif Yılmaz - 05389012345'),
                                                                                              (302, 'O+', '1992-11-19', 'SGK-1234567989', 'Kemal Can - 05490123456'),

                                                                                              (303, 'B+', '1986-06-12', 'SGK-1234567990', 'Merve Demir - 05301234567'),
                                                                                              (304, 'A+', '1994-01-05', 'SGK-1234567991', 'Can Kaya - 05512345678'),
                                                                                              (305, 'O-', '1988-08-28', 'SGK-1234567992', 'Seda Şahin - 05323456789'),
                                                                                              (306, 'AB+', '1991-03-21', 'SGK-1234567993', 'Burak Arslan - 05434567890'),
                                                                                              (307, 'A+', '1996-10-14', 'SGK-1234567994', 'Deniz Öztürk - 05345678901'),
                                                                                              (308, 'B+', '1984-05-07', 'SGK-1234567995', 'Cemre Çelik - 05456789012'),
                                                                                              (309, 'O+', '1993-12-30', 'SGK-1234567996', 'Eren Yıldız - 05367890123'),
                                                                                              (310, 'A+', '1990-07-23', 'SGK-1234567997', 'Aylin Korkmaz - 05478901234'),
                                                                                              (311, 'AB+', '1987-02-16', 'SGK-1234567998', 'Serkan Aydın - 05389012345'),
                                                                                              (312, 'O+', '1995-09-09', 'SGK-1234567999', 'Gamze Demirtaş - 05490123456'),

                                                                                              (313, 'A+', '1989-04-02', 'SGK-1234568000', 'Hakan Şen - 05301234567'),
                                                                                              (314, 'B+', '1992-11-25', 'SGK-1234568001', 'Nazan Güler - 05512345678'),
                                                                                              (315, 'O+', '1986-06-18', 'SGK-1234568002', 'Orhan Sarı - 05323456789'),
                                                                                              (316, 'AB-', '1994-01-11', 'SGK-1234568003', 'Perihan Koç - 05434567890'),
                                                                                              (317, 'A+', '1988-08-04', 'SGK-1234568004', 'Rıza Yılmaz - 05345678901'),
                                                                                              (318, 'B+', '1991-03-27', 'SGK-1234568005', 'Selma Can - 05456789012'),
                                                                                              (319, 'O+', '1997-10-20', 'SGK-1234568006', 'Taner Demir - 05367890123'),
                                                                                              (320, 'A+', '1985-05-13', 'SGK-1234568007', 'Uğur Kaya - 05478901234'),
                                                                                              (321, 'AB+', '1993-12-06', 'SGK-1234568008', 'Vildan Şahin - 05389012345'),
                                                                                              (322, 'O+', '1990-07-29', 'SGK-1234568009', 'Yusuf Arslan - 05490123456'),

                                                                                              (323, 'B+', '1987-02-22', 'SGK-1234568010', 'Zehra Öztürk - 05301234567'),
                                                                                              (324, 'A+', '1995-09-15', 'SGK-1234568011', 'Ahmet Çelik - 05512345678'),
                                                                                              (325, 'O+', '1989-04-08', 'SGK-1234568012', 'Ayşe Yıldız - 05323456789'),
                                                                                              (326, 'AB+', '1992-11-01', 'SGK-1234568013', 'Mehmet Korkmaz - 05434567890'),
                                                                                              (327, 'A-', '1986-06-24', 'SGK-1234568014', 'Fatma Aydın - 05345678901'),
                                                                                              (328, 'B+', '1994-01-17', 'SGK-1234568015', 'Ali Demirtaş - 05456789012'),
                                                                                              (329, 'O+', '1988-08-10', 'SGK-1234568016', 'Zeynep Şen - 05367890123'),
                                                                                              (330, 'A+', '1991-03-03', 'SGK-1234568017', 'Mustafa Güler - 05478901234'),
                                                                                              (331, 'AB+', '1997-10-26', 'SGK-1234568018', 'Elif Sarı - 05389012345'),
                                                                                              (332, 'O-', '1985-05-19', 'SGK-1234568019', 'Kemal Koç - 05490123456'),

                                                                                              (333, 'A+', '1993-12-12', 'SGK-1234568020', 'Merve Yılmaz - 05301234567'),
                                                                                              (334, 'B+', '1990-07-05', 'SGK-1234568021', 'Can Can - 05512345678'),
                                                                                              (335, 'O+', '1987-02-28', 'SGK-1234568022', 'Seda Demir - 05323456789'),
                                                                                              (336, 'AB+', '1995-09-21', 'SGK-1234568023', 'Burak Kaya - 05434567890'),
                                                                                              (337, 'A+', '1989-04-14', 'SGK-1234568024', 'Deniz Şahin - 05345678901'),
                                                                                              (338, 'B-', '1992-11-07', 'SGK-1234568025', 'Cemre Arslan - 05456789012'),
                                                                                              (339, 'O+', '1986-06-30', 'SGK-1234568026', 'Eren Öztürk - 05367890123'),
                                                                                              (340, 'A+', '1994-01-23', 'SGK-1234568027', 'Aylin Çelik - 05478901234'),
                                                                                              (341, 'AB+', '1988-08-16', 'SGK-1234568028', 'Serkan Yıldız - 05389012345'),
                                                                                              (342, 'O+', '1991-03-09', 'SGK-1234568029', 'Gamze Korkmaz - 05490123456'),

                                                                                              (343, 'B+', '1996-10-02', 'SGK-1234568030', 'Hakan Aydın - 05301234567'),
                                                                                              (344, 'A+', '1984-05-25', 'SGK-1234568031', 'Nazan Demirtaş - 05512345678'),
                                                                                              (345, 'O-', '1993-12-18', 'SGK-1234568032', 'Orhan Şen - 05323456789'),
                                                                                              (346, 'AB+', '1990-07-11', 'SGK-1234568033', 'Perihan Güler - 05434567890'),
                                                                                              (347, 'A+', '1987-02-04', 'SGK-1234568034', 'Rıza Sarı - 05345678901'),
                                                                                              (348, 'B+', '1995-09-27', 'SGK-1234568035', 'Selma Koç - 05456789012'),
                                                                                              (349, 'O+', '1989-04-20', 'SGK-1234568036', 'Taner Yılmaz - 05367890123'),
                                                                                              (350, 'A+', '1992-11-13', 'SGK-1234568037', 'Uğur Can - 05478901234'),
                                                                                              (351, 'AB-', '1986-06-06', 'SGK-1234568038', 'Vildan Demir - 05389012345'),
                                                                                              (352, 'O+', '1994-01-29', 'SGK-1234568039', 'Yusuf Kaya - 05490123456'),

                                                                                              (353, 'A+', '1988-08-22', 'SGK-1234568040', 'Zehra Şahin - 05301234567'),
                                                                                              (354, 'B+', '1991-03-15', 'SGK-1234568041', 'Ahmet Arslan - 05512345678'),
                                                                                              (355, 'O+', '1997-10-08', 'SGK-1234568042', 'Ayşe Öztürk - 05323456789'),
                                                                                              (356, 'AB+', '1985-05-01', 'SGK-1234568043', 'Mehmet Çelik - 05434567890'),
                                                                                              (357, 'A-', '1993-12-24', 'SGK-1234568044', 'Fatma Yıldız - 05345678901'),
                                                                                              (358, 'B+', '1990-07-17', 'SGK-1234568045', 'Ali Korkmaz - 05456789012'),
                                                                                              (359, 'O+', '1987-02-10', 'SGK-1234568046', 'Zeynep Aydın - 05367890123'),
                                                                                              (360, 'A+', '1995-09-03', 'SGK-1234568047', 'Mustafa Demirtaş - 05478901234'),
                                                                                              (361, 'AB+', '1989-04-26', 'SGK-1234568048', 'Elif Şen - 05389012345'),
                                                                                              (362, 'O+', '1992-11-19', 'SGK-1234568049', 'Kemal Güler - 05490123456'),

                                                                                              (363, 'B+', '1986-06-12', 'SGK-1234568050', 'Merve Sarı - 05301234567'),
                                                                                              (364, 'A+', '1994-01-05', 'SGK-1234568051', 'Can Koç - 05512345678'),
                                                                                              (365, 'O-', '1988-08-28', 'SGK-1234568052', 'Seda Yılmaz - 05323456789'),
                                                                                              (366, 'AB+', '1991-03-21', 'SGK-1234568053', 'Burak Can - 05434567890'),
                                                                                              (367, 'A+', '1996-10-14', 'SGK-1234568054', 'Deniz Demir - 05345678901'),
                                                                                              (368, 'B+', '1984-05-07', 'SGK-1234568055', 'Cemre Kaya - 05456789012'),
                                                                                              (369, 'O+', '1993-12-30', 'SGK-1234568056', 'Eren Şahin - 05367890123'),
                                                                                              (370, 'A+', '1990-07-23', 'SGK-1234568057', 'Aylin Arslan - 05478901234'),
                                                                                              (371, 'AB+', '1987-02-16', 'SGK-1234568058', 'Serkan Öztürk - 05389012345'),
                                                                                              (372, 'O+', '1995-09-09', 'SGK-1234568059', 'Gamze Çelik - 05490123456'),

                                                                                              (373, 'A+', '1989-04-02', 'SGK-1234568060', 'Hakan Yıldız - 05301234567'),
                                                                                              (374, 'B+', '1992-11-25', 'SGK-1234568061', 'Nazan Korkmaz - 05512345678'),
                                                                                              (375, 'O+', '1986-06-18', 'SGK-1234568062', 'Orhan Aydın - 05323456789'),
                                                                                              (376, 'AB-', '1994-01-11', 'SGK-1234568063', 'Perihan Demirtaş - 05434567890'),
                                                                                              (377, 'A+', '1988-08-04', 'SGK-1234568064', 'Rıza Şen - 05345678901');

-- -----------------------------------------------------
-- 8. AVAILABILITY (Rastgele Dağılımlı)
-- -----------------------------------------------------
-- Her doktor için farklı sayıda ve farklı tarihlerde müsaitlik slotları

INSERT INTO Availability (doctor_id, date, time_slot, is_booked) VALUES
-- İstanbul 1. Hastane - Doktor 1-7
-- Doktor 1: 12 slot
(1, '2025-11-01', '09:00:00', TRUE),
(1, '2025-11-01', '10:30:00', TRUE),
(1, '2025-11-01', '14:00:00', FALSE),
(1, '2025-11-02', '09:00:00', TRUE),
(1, '2025-11-02', '11:00:00', TRUE),
(1, '2025-11-03', '09:00:00', FALSE),
(1, '2025-11-03', '15:00:00', TRUE),
(1, '2025-11-04', '10:00:00', FALSE),
(1, '2025-11-05', '09:00:00', TRUE),
(1, '2025-11-05', '14:00:00', FALSE),
(1, '2025-11-06', '11:00:00', FALSE),
(1, '2025-11-07', '09:00:00', TRUE),

-- Doktor 2: 8 slot
(2, '2025-11-01', '10:00:00', TRUE),
(2, '2025-11-01', '15:00:00', FALSE),
(2, '2025-11-02', '09:00:00', TRUE),
(2, '2025-11-03', '10:00:00', FALSE),
(2, '2025-11-04', '14:00:00', TRUE),
(2, '2025-11-05', '09:00:00', FALSE),
(2, '2025-11-06', '10:00:00', TRUE),
(2, '2025-11-08', '15:00:00', FALSE),

-- Doktor 3: 15 slot
(3, '2025-11-01', '09:00:00', TRUE),
(3, '2025-11-01', '10:00:00', TRUE),
(3, '2025-11-01', '11:00:00', TRUE),
(3, '2025-11-01', '14:00:00', FALSE),
(3, '2025-11-02', '09:00:00', TRUE),
(3, '2025-11-02', '10:30:00', TRUE),
(3, '2025-11-02', '15:00:00', FALSE),
(3, '2025-11-03', '09:00:00', TRUE),
(3, '2025-11-03', '14:00:00', FALSE),
(3, '2025-11-04', '10:00:00', TRUE),
(3, '2025-11-05', '09:00:00', FALSE),
(3, '2025-11-05', '11:00:00', TRUE),
(3, '2025-11-06', '14:00:00', FALSE),
(3, '2025-11-07', '09:00:00', TRUE),
(3, '2025-11-08', '10:00:00', FALSE),

-- Doktor 4: 6 slot
(4, '2025-11-01', '14:00:00', TRUE),
(4, '2025-11-02', '15:00:00', FALSE),
(4, '2025-11-03', '14:00:00', TRUE),
(4, '2025-11-04', '15:00:00', FALSE),
(4, '2025-11-06', '14:00:00', TRUE),
(4, '2025-11-07', '15:00:00', FALSE),

-- Doktor 5: 10 slot
(5, '2025-11-01', '09:00:00', TRUE),
(5, '2025-11-01', '10:00:00', TRUE),
(5, '2025-11-01', '11:00:00', FALSE),
(5, '2025-11-02', '09:00:00', TRUE),
(5, '2025-11-03', '10:00:00', FALSE),
(5, '2025-11-04', '09:00:00', TRUE),
(5, '2025-11-05', '11:00:00', FALSE),
(5, '2025-11-06', '09:00:00', TRUE),
(5, '2025-11-07', '10:00:00', FALSE),
(5, '2025-11-08', '09:00:00', TRUE),

-- Doktor 6: 5 slot
(6, '2025-11-02', '14:00:00', FALSE),
(6, '2025-11-03', '15:00:00', FALSE),
(6, '2025-11-05', '14:00:00', TRUE),
(6, '2025-11-06', '15:00:00', FALSE),
(6, '2025-11-08', '14:00:00', FALSE),

-- Doktor 7: 9 slot
(7, '2025-11-01', '10:00:00', TRUE),
(7, '2025-11-01', '11:00:00', FALSE),
(7, '2025-11-02', '10:00:00', TRUE),
(7, '2025-11-03', '11:00:00', FALSE),
(7, '2025-11-04', '10:00:00', TRUE),
(7, '2025-11-05', '10:00:00', FALSE),
(7, '2025-11-06', '11:00:00', TRUE),
(7, '2025-11-07', '10:00:00', FALSE),
(7, '2025-11-08', '11:00:00', TRUE),

-- İstanbul 2. Hastane - Doktor 8-14
-- Doktor 8: 11 slot
(8, '2025-11-01', '09:00:00', TRUE),
(8, '2025-11-01', '14:00:00', TRUE),
(8, '2025-11-02', '09:00:00', FALSE),
(8, '2025-11-03', '10:00:00', TRUE),
(8, '2025-11-04', '09:00:00', FALSE),
(8, '2025-11-05', '14:00:00', TRUE),
(8, '2025-11-06', '09:00:00', FALSE),
(8, '2025-11-07', '10:00:00', TRUE),
(8, '2025-11-08', '14:00:00', FALSE),
(8, '2025-11-09', '09:00:00', TRUE),
(8, '2025-11-10', '10:00:00', FALSE),

-- Doktor 9: 4 slot
(9, '2025-11-02', '15:00:00', FALSE),
(9, '2025-11-04', '15:00:00', FALSE),
(9, '2025-11-06', '15:00:00', TRUE),
(9, '2025-11-08', '15:00:00', FALSE),

-- Doktor 10: 13 slot
(10, '2025-11-01', '10:00:00', TRUE),
(10, '2025-11-01', '11:00:00', TRUE),
(10, '2025-11-01', '15:00:00', FALSE),
(10, '2025-11-02', '10:00:00', TRUE),
(10, '2025-11-02', '14:00:00', FALSE),
(10, '2025-11-03', '10:00:00', TRUE),
(10, '2025-11-04', '11:00:00', FALSE),
(10, '2025-11-05', '10:00:00', TRUE),
(10, '2025-11-06', '15:00:00', FALSE),
(10, '2025-11-07', '10:00:00', TRUE),
(10, '2025-11-08', '11:00:00', FALSE),
(10, '2025-11-09', '14:00:00', TRUE),
(10, '2025-11-10', '10:00:00', FALSE),

-- Doktor 11: 14 slot
(11, '2025-11-01', '09:00:00', TRUE),
(11, '2025-11-01', '10:00:00', TRUE),
(11, '2025-11-01', '11:00:00', TRUE),
(11, '2025-11-01', '14:00:00', FALSE),
(11, '2025-11-02', '09:00:00', TRUE),
(11, '2025-11-02', '10:00:00', TRUE),
(11, '2025-11-03', '09:00:00', FALSE),
(11, '2025-11-04', '10:00:00', TRUE),
(11, '2025-11-05', '09:00:00', FALSE),
(11, '2025-11-06', '10:00:00', TRUE),
(11, '2025-11-07', '11:00:00', FALSE),
(11, '2025-11-08', '09:00:00', TRUE),
(11, '2025-11-09', '10:00:00', FALSE),
(11, '2025-11-10', '09:00:00', TRUE),

-- Doktor 12: 7 slot
(12, '2025-11-01', '14:00:00', TRUE),
(12, '2025-11-02', '15:00:00', FALSE),
(12, '2025-11-04', '14:00:00', TRUE),
(12, '2025-11-05', '15:00:00', FALSE),
(12, '2025-11-07', '14:00:00', TRUE),
(12, '2025-11-08', '15:00:00', FALSE),
(12, '2025-11-09', '14:00:00', TRUE),

-- Doktor 13: 3 slot
(13, '2025-11-03', '10:00:00', FALSE),
(13, '2025-11-06', '10:00:00', FALSE),
(13, '2025-11-09', '10:00:00', FALSE),

-- Doktor 14: 10 slot
(14, '2025-11-01', '14:00:00', TRUE),
(14, '2025-11-01', '15:00:00', TRUE),
(14, '2025-11-02', '14:00:00', FALSE),
(14, '2025-11-03', '15:00:00', TRUE),
(14, '2025-11-04', '14:00:00', FALSE),
(14, '2025-11-05', '15:00:00', TRUE),
(14, '2025-11-06', '14:00:00', FALSE),
(14, '2025-11-07', '15:00:00', TRUE),
(14, '2025-11-08', '14:00:00', FALSE),
(14, '2025-11-09', '15:00:00', TRUE),

-- İstanbul 3. Hastane - Doktor 15-21
-- Doktor 15: 8 slot
(15, '2025-11-01', '10:00:00', TRUE),
(15, '2025-11-02', '11:00:00', FALSE),
(15, '2025-11-03', '10:00:00', TRUE),
(15, '2025-11-05', '11:00:00', FALSE),
(15, '2025-11-06', '10:00:00', TRUE),
(15, '2025-11-07', '11:00:00', FALSE),
(15, '2025-11-08', '10:00:00', TRUE),
(15, '2025-11-09', '11:00:00', FALSE),

-- Doktor 16-35: Availability kayıtları (Her doktor için 5-12 arası rastgele slot)
(16, '2025-11-02', '09:00:00', TRUE), (16, '2025-11-03', '10:00:00', FALSE), (16, '2025-11-05', '09:00:00', TRUE),
(16, '2025-11-07', '10:00:00', FALSE), (16, '2025-11-08', '09:00:00', TRUE), (16, '2025-11-10', '10:00:00', FALSE),

(17, '2025-11-01', '14:00:00', TRUE), (17, '2025-11-02', '15:00:00', FALSE), (17, '2025-11-03', '14:00:00', TRUE),
(17, '2025-11-04', '15:00:00', FALSE), (17, '2025-11-05', '14:00:00', TRUE), (17, '2025-11-06', '15:00:00', FALSE),
(17, '2025-11-07', '14:00:00', TRUE), (17, '2025-11-08', '15:00:00', FALSE), (17, '2025-11-09', '14:00:00', TRUE),

(18, '2025-11-02', '10:00:00', FALSE), (18, '2025-11-04', '11:00:00', FALSE), (18, '2025-11-06', '10:00:00', TRUE),
(18, '2025-11-08', '11:00:00', FALSE), (18, '2025-11-10', '10:00:00', FALSE),

(19, '2025-11-01', '09:00:00', TRUE), (19, '2025-11-01', '10:00:00', TRUE), (19, '2025-11-02', '09:00:00', FALSE),
(19, '2025-11-03', '10:00:00', TRUE), (19, '2025-11-04', '09:00:00', FALSE), (19, '2025-11-05', '10:00:00', TRUE),
(19, '2025-11-06', '09:00:00', FALSE), (19, '2025-11-07', '10:00:00', TRUE), (19, '2025-11-08', '09:00:00', FALSE),
(19, '2025-11-09', '10:00:00', TRUE), (19, '2025-11-10', '09:00:00', FALSE), (19, '2025-11-11', '10:00:00', TRUE),

(20, '2025-11-03', '14:00:00', FALSE), (20, '2025-11-05', '15:00:00', FALSE), (20, '2025-11-08', '14:00:00', TRUE),
(20, '2025-11-10', '15:00:00', FALSE),

(21, '2025-11-01', '11:00:00', TRUE), (21, '2025-11-03', '10:00:00', FALSE), (21, '2025-11-04', '11:00:00', TRUE),
(21, '2025-11-06', '10:00:00', FALSE), (21, '2025-11-07', '11:00:00', TRUE), (21, '2025-11-09', '10:00:00', FALSE),
(21, '2025-11-10', '11:00:00', TRUE),

-- İstanbul 4. Hastane - Doktor 22-28
(22, '2025-11-01', '09:00:00', TRUE), (22, '2025-11-02', '10:00:00', FALSE), (22, '2025-11-03', '09:00:00', TRUE),
(22, '2025-11-05', '10:00:00', FALSE), (22, '2025-11-07', '09:00:00', TRUE), (22, '2025-11-08', '10:00:00', FALSE),
(22, '2025-11-10', '09:00:00', TRUE), (22, '2025-11-11', '10:00:00', FALSE),

(23, '2025-11-01', '14:00:00', TRUE), (23, '2025-11-02', '15:00:00', FALSE), (23, '2025-11-04', '14:00:00', TRUE),
(23, '2025-11-06', '15:00:00', FALSE), (23, '2025-11-08', '14:00:00', TRUE), (23, '2025-11-09', '15:00:00', FALSE),

(24, '2025-11-01', '10:00:00', TRUE), (24, '2025-11-03', '11:00:00', FALSE), (24, '2025-11-05', '10:00:00', TRUE),
(24, '2025-11-07', '11:00:00', FALSE), (24, '2025-11-09', '10:00:00', TRUE),

(25, '2025-11-02', '09:00:00', TRUE), (25, '2025-11-03', '10:00:00', FALSE), (25, '2025-11-05', '09:00:00', TRUE),
(25, '2025-11-07', '10:00:00', FALSE), (25, '2025-11-09', '09:00:00', TRUE), (25, '2025-11-10', '10:00:00', FALSE),
(25, '2025-11-11', '09:00:00', TRUE),

(26, '2025-11-01', '14:00:00', TRUE), (26, '2025-11-03', '15:00:00', FALSE), (26, '2025-11-05', '14:00:00', TRUE),
(26, '2025-11-07', '15:00:00', FALSE), (26, '2025-11-09', '14:00:00', TRUE),

(27, '2025-11-02', '10:00:00', TRUE), (27, '2025-11-04', '11:00:00', FALSE), (27, '2025-11-06', '10:00:00', TRUE),
(27, '2025-11-08', '11:00:00', FALSE), (27, '2025-11-10', '10:00:00', TRUE), (27, '2025-11-11', '11:00:00', FALSE),

(28, '2025-11-01', '09:00:00', TRUE), (28, '2025-11-02', '10:00:00', FALSE), (28, '2025-11-04', '09:00:00', TRUE),
(28, '2025-11-06', '10:00:00', FALSE), (28, '2025-11-08', '09:00:00', TRUE),

-- İstanbul 5. Hastane - Doktor 29-35
(29, '2025-11-01', '14:00:00', TRUE), (29, '2025-11-02', '15:00:00', FALSE), (29, '2025-11-04', '14:00:00', TRUE),
(29, '2025-11-06', '15:00:00', FALSE), (29, '2025-11-08', '14:00:00', TRUE), (29, '2025-11-10', '15:00:00', FALSE),

(30, '2025-11-01', '10:00:00', TRUE), (30, '2025-11-03', '11:00:00', FALSE), (30, '2025-11-05', '10:00:00', TRUE),
(30, '2025-11-07', '11:00:00', FALSE), (30, '2025-11-09', '10:00:00', TRUE), (30, '2025-11-11', '11:00:00', FALSE),

(31, '2025-11-02', '09:00:00', TRUE), (31, '2025-11-04', '10:00:00', FALSE), (31, '2025-11-06', '09:00:00', TRUE),
(31, '2025-11-08', '10:00:00', FALSE), (31, '2025-11-10', '09:00:00', TRUE),

(32, '2025-11-01', '14:00:00', TRUE), (32, '2025-11-03', '15:00:00', FALSE), (32, '2025-11-05', '14:00:00', TRUE),
(32, '2025-11-07', '15:00:00', FALSE), (32, '2025-11-09', '14:00:00', TRUE), (32, '2025-11-11', '15:00:00', FALSE),

(33, '2025-11-02', '10:00:00', TRUE), (33, '2025-11-04', '11:00:00', FALSE), (33, '2025-11-06', '10:00:00', TRUE),
(33, '2025-11-08', '11:00:00', FALSE), (33, '2025-11-10', '10:00:00', TRUE),

(34, '2025-11-01', '09:00:00', TRUE), (34, '2025-11-03', '10:00:00', FALSE), (34, '2025-11-05', '09:00:00', TRUE),
(34, '2025-11-07', '10:00:00', FALSE), (34, '2025-11-09', '09:00:00', TRUE), (34, '2025-11-11', '10:00:00', FALSE),

(35, '2025-11-02', '14:00:00', TRUE), (35, '2025-11-04', '15:00:00', FALSE), (35, '2025-11-06', '14:00:00', TRUE),
(35, '2025-11-08', '15:00:00', FALSE), (35, '2025-11-10', '14:00:00', TRUE);

-- Kalan doktorlar için availability (36-175) - Basitleştirilmiş örnekler
-- Her doktor için 5-8 slot

-- APPOINTMENT bölümünü şu şekilde güncelleyin:

-- 9. APPOINTMENT (Randevular - Availability ile ilişkili)
INSERT INTO Appointment (doctor_id, patient_id, availability_id, hospital_id, status, notes, diagnosis, prescription) VALUES
-- İstanbul 1. Hastane Randevuları (patient_id: 1-10)
(1, 1, 1, 1, 'completed', 'Kontrol muayenesi, tansiyon takibi', 'Hipertansiyon', 'Ramipril 5mg 1x1, Tuz kısıtlaması'),
(1, 2, 2, 1, 'completed', 'Göğüs ağrısı şikayeti', 'Koroner arter hastalığı şüphesi', 'EKG ve Efor Testi istendi, Aspirin 100mg'),
(1, 3, 5, 1, 'completed', 'EKG ve kan tahlili sonuçları değerlendirildi', 'Stabil angina pectoris', 'Atorvastatin 20mg, Metoprolol 50mg'),
(1, 4, 7, 1, 'completed', 'Kardiyolojik check-up', 'Normal kardiyak bulgular', 'Öneri yok, 6 ay sonra kontrol'),
(1, 5, 9, 1, 'completed', 'Rutin kontrol', 'Kontrollü hipertansiyon', 'Mevcut tedaviye devam'),
(1, 6, 12, 1, 'scheduled', 'Tekrar kontrol', NULL, NULL),

(2, 7, 13, 1, 'completed', 'Diz ağrısı şikayeti', 'Medial menisküs yırtığı', 'Fizik Tedavi 10 seans, İbuprofen 400mg'),
(2, 8, 15, 1, 'completed', 'MR sonuçları değerlendirildi', 'Gonartroz', 'Glukozamin, Asetaminofen 500mg'),
(2, 9, 18, 1, 'completed', 'Fizik tedavi takibi', 'İyileşme devam ediyor', 'Fizik tedaviye devam'),
(2, 10, 20, 1, 'scheduled', 'Kontrol muayenesi', NULL, NULL),

(3, 11, 21, 1, 'completed', 'Baş ağrısı şikayeti', 'Migren', 'Sumatriptan 50mg ihtiyaç halinde'),
(3, 12, 22, 1, 'completed', 'EEG çekildi', 'Normal EEG bulguları', 'Stres yönetimi önerildi'),
(3, 13, 23, 1, 'completed', 'Nörolojik muayene', 'Tension tip baş ağrısı', 'Amytriptiline 10mg gece'),
(3, 14, 26, 1, 'completed', 'MR sonuçları değerlendirildi', 'Normal MR bulguları', 'Mevcut tedaviye devam'),
(3, 15, 27, 1, 'completed', 'Tedavi planı oluşturuldu', 'Kronik baş ağrısı', 'Topiramate 25mg 2x1'),
(3, 16, 30, 1, 'completed', 'Kontrol', 'İyileşme var', 'Tedavi dozu ayarlandı'),
(3, 17, 33, 1, 'completed', 'İlaç doz ayarlaması', 'Stabil nörolojik durum', 'Topiramate 50mg 2x1'),
(3, 18, 35, 1, 'scheduled', 'Tekrar kontrol', NULL, NULL),

-- İstanbul 2. Hastane Randevuları (patient_id: 19-30)
(8, 19, 36, 2, 'completed', 'Çarpıntı şikayeti', 'Paroksismal atrial taşikardi', 'Metoprolol 50mg 2x1'),
(8, 20, 37, 2, 'completed', 'Holter EKG takıldı', 'Holter incelemesi devam', 'Geçici rapor'),
(8, 21, 40, 2, 'completed', 'Holter sonuçları değerlendirildi', 'Frekans ventriküler ekstrasistol', 'Propafenone 150mg 3x1'),
(8, 22, 43, 2, 'completed', 'Tedavi başlandı', 'Kardiyak aritmi', 'Mevcut tedaviye devam'),
(8, 23, 46, 2, 'completed', 'Kontrol muayenesi', 'Kontrollü aritmi', 'Tedavi dozu optimize edildi'),

(9, 24, 50, 2, 'completed', 'Omuz ağrısı şikayeti', 'Rotator manşet tendiniti', 'Naproksen 500mg, Fizik tedavi'),

(10, 25, 51, 2, 'completed', 'Baş dönmesi şikayeti', 'Vertigo', 'Betahistine 16mg 3x1'),
(10, 26, 52, 2, 'completed', 'Nörolojik muayene', 'Benign paroksismal pozisyonel vertigo', 'Epley manevrası uygulandı'),
(10, 27, 55, 2, 'completed', 'Kan tahlilleri yapıldı', 'B12 eksikliği', 'B12 enjeksiyonu haftada 1'),
(10, 28, 58, 2, 'completed', 'Tedavi planı oluşturuldu', 'Kronik vertigo', 'Vestibüler rehabilitasyon'),
(10, 29, 61, 2, 'scheduled', 'Kontrol', NULL, NULL),

-- Diğer hastanelerden örnek randevular (patient_id: 30-50)
(15, 30, 69, 3, 'completed', 'Göz muayenesi', 'Miyopi', 'Gözlük reçetesi: Sağ -2.00, Sol -1.75'),
(15, 31, 72, 3, 'completed', 'Göz tansiyonu ölçümü', 'Oküler hipertansiyon', 'Göz damlası 2x1'),
(15, 32, 75, 3, 'completed', 'Kontrol muayenesi', 'Stabil göz basıncı', 'Mevcut tedaviye devam'),

(16, 33, 76, 3, 'completed', 'Cilt lezyonu değerlendirmesi', 'Benign nevüs', 'Kriyoterapi uygulandı'),
(16, 34, 79, 3, 'completed', 'Biyopsi sonuçları değerlendirildi', 'Seboreik keratoz', 'Kriyoterapi ile tedavi edildi'),
(16, 35, 81, 3, 'scheduled', 'Tedavi takibi', NULL, NULL);

-- 10. MEDICAL_RECORD (Tıbbi Kayıtlar - 50 kayıt)
INSERT INTO Medical_Record (patient_id, doctor_id, appointment_id, hospital_id, record_date, test_results, medications, notes) VALUES
-- 1-10. kayıtlar
(1, 1, 1, 1, '2025-11-01', 'EKG: Normal Sinüs Ritmi, Tansiyon: 145/90', 'Ramipril 5mg 1x1', 'Tuz kısıtlaması önerildi, 3 ay sonra kontrol'),
(2, 1, 2, 1, '2025-11-01', 'EKG: ST segment depresyonu, Troponin: Negatif', 'EKG ve Efor Testi yapılacak', 'Koroner arter hastalığı şüphesi, kardiyoloji takip'),
(3, 2, 3, 1, '2025-11-01', 'MR: Medial menisküs yırtığı', 'Fizik Tedavi 10 Seans + NSAID', 'Cerrahi gerekmeyebilir, konservatif tedavi denenecek'),
(4, 3, 4, 1, '2025-11-01', 'EEG: Normal, Kan testleri: Normal', 'Sumatriptan 50mg ihtiyaç halinde', 'Migren atakları için tetikleyicilerden kaçınılması önerildi'),
(5, 4, 5, 1, '2025-11-02', 'CRP: 12 mg/L, Lökosit: 14.000', 'Parasetamol şurup 4x1, Amoksisilin 3x1', 'Üst solunum yoku enfeksiyonu, 5 gün sonra kontrol'),
(6, 5, 6, 1, '2025-11-02', 'Göz dibi muayenesi: Normal, Görme keskinliği: 0.7', 'Gözlük reçetesi: Sağ -2.00, Sol -1.75', '6 ay sonra göz kontrolü'),
(7, 7, 7, 1, '2025-11-02', 'Psikiyatrik değerlendirme: Depresyon', 'Sertraline 50mg 1x1', 'Haftalık terapi seansları önerildi'),
(8, 8, 8, 2, '2025-11-03', 'Holter EKG: Paroksismal atrial taşikardi', 'Metoprolol 50mg 2x1', 'Kafein ve stresden kaçınılması önerildi'),
(9, 10, 9, 2, '2025-11-03', 'USG: Rotator manşet tendiniti', 'Naproksen 500mg 2x1, Fizik tedavi', 'Kol istirahati, ağır kaldırma yasak'),
(10, 11, 10, 2, '2025-11-03', 'B12: 180 pg/mL, Folat: Normal', 'B12 enjeksiyonu haftada 1', '3 ay sonra B12 seviyesi kontrolü'),

-- 11-20. kayıtlar
(11, 14, NULL, 2, '2025-11-04', 'Alerji testi: Polen alerjisi pozitif', 'Loratadin 10mg 1x1, Nazal steroid', 'Alerjen maruziyetinden kaçınılması önerildi'),
(12, 15, NULL, 3, '2025-11-04', 'Psikiyatrik değerlendirme: Anksiyete bozukluğu', 'Bilişsel davranışçı terapi', 'Nefes egzersizleri öğretildi'),
(13, 16, NULL, 6, '2025-11-05', 'EKG: Normal, Stres testi: Normal', 'Öneri yok', 'Stres yönetimi ve düzenli egzersiz önerildi'),
(14, 17, 11, 6, '2025-11-05', 'MR: L4-L5 bel fıtığı', 'Fizik tedavi, İbuprofen 400mg 3x1', 'Ağır kaldırma yasak, düzenli egzersiz'),
(15, 18, NULL, 11, '2025-11-06', 'EKG: Sinüs taşikardisi, Tansiyon: 140/85', 'Metoprolol 25mg 2x1', 'Tuz kısıtlaması, düzenli yürüyüş'),
(16, 19, NULL, 12, '2025-11-06', 'Röntgen: Diz protezi pozisyonu normal', 'Fizik tedavi devam', 'Düzenli yürüyüş, kilo kontrolü önemli'),
(17, 20, NULL, 16, '2025-11-07', 'EKG: İskemi bulguları, Anjiyo: %50 LAD darlığı', 'Aspirin 100mg, Atorvastatin 20mg', 'Kardiyak rehabilitasyon programına alındı'),
(18, 21, NULL, 17, '2025-11-07', 'MR: Rotator manşet tam kat yırtık', 'Cerrahi öncesi fizik tedavi', 'Cerrahi için hazırlık, 2 hafta sonra kontrol'),
(19, 22, 12, 21, '2025-11-08', 'Spirometri: Hafif obstrüktif pattern', 'Salmeterol/Flutikazon inhaler 2x1', 'Sigara bırakma programı önerildi'),
(20, 23, 13, 22, '2025-11-08', 'Röntgen: Kalça osteoartriti', 'Asetaminofen 500mg 3x1, Glukozamin', 'Kilo verme, yüzme egzersizi önerildi'),

-- 21-30. kayıtlar
(21, 24, NULL, 3, '2025-11-09', 'EEG: Normal, Kan testleri: Normal', 'İbuprofen 400mg ihtiyaç halinde', 'Stres yönetimi ve düzenli uyku önerildi'),
(22, 25, NULL, 8, '2025-11-09', 'MMSE: 28/30, Kan testleri: Normal', 'B12 takviyesi, Omega-3', 'Mental egzersizler, bulmaca çözme önerildi'),
(23, 26, NULL, 13, '2025-11-10', 'Akciğer grafisi: Bronşit bulguları', 'Amoksisilin 3x1, Öksürük şurup', 'Bol sıvı tüketimi, istirahat önerildi'),
(24, 27, NULL, 18, '2025-11-10', 'RF: Pozitif, CRP: 25 mg/L', 'Metotreksat 15mg/hafta, Folik asit', 'Romatoloji takibi, 1 ay sonra kontrol'),
(25, 28, NULL, 23, '2025-11-11', 'Schirmer testi: 5mm/5dk', 'Suni gözyaşı 4x1, Omega-3 takviyesi', 'Ekran kullanımını azaltma önerildi'),
(26, 29, NULL, 25, '2025-11-11', 'Psikiyatrik değerlendirme: Anksiyete', 'Sertraline 50mg 1x1, Terapi', 'Haftalık terapi seansları başlatıldı'),
(27, 1, NULL, 1, '2025-11-12', 'Kan testleri: Kolesterol 240 mg/dL', 'Atorvastatin 20mg 1x1', 'Diyet ve egzersiz önerildi, 3 ay sonra kontrol'),
(28, 3, NULL, 1, '2025-11-12', 'EMG: Karpal tünel sendromu', 'Bileklik, Anti-inflamatuar ilaç', 'Cerrahi konsültasyon önerildi'),
(29, 5, NULL, 1, '2025-11-13', 'Göz basıncı: 18 mmHg', 'Göz damlası 2x1', 'Glokom şüphesi, 1 ay sonra kontrol'),
(30, 8, NULL, 2, '2025-11-13', 'EKO: EF %55, Hafif mitral yetmezlik', 'Ramipril 5mg 1x1', '6 ay sonra kardiyolojik kontrol'),

-- 31-40. kayıtlar
(31, 11, NULL, 2, '2025-11-14', 'MR Beyin: Normal, B12: 350 pg/mL', 'B12 takviyesi devam', '3 ay sonra nörolojik kontrol'),
(32, 14, NULL, 2, '2025-11-14', 'Patch test: Nikel alerjisi pozitif', 'Topikal steroid, Antihistaminik', 'Nikel içeren takılardan kaçınılması'),
(33, 16, NULL, 6, '2025-11-15', 'EKG: Normal, Tansiyon: 135/80', 'Öneri yok', 'Düzenli egzersiz ve diyet önerildi'),
(34, 18, NULL, 11, '2025-11-15', 'Holter: İzole PVCler', 'Öneri yok', 'Kafein kısıtlaması, stres yönetimi'),
(35, 20, NULL, 16, '2025-11-16', 'EKO: EF %60, Normal kardiyak yapı', 'Aspirin 100mg devam', '6 ay sonra kardiyolojik kontrol'),
(36, 22, NULL, 21, '2025-11-16', 'Spirometri: Normal', 'İnhaler ihtiyaç halinde', 'Sigara bırakma başarılı, 6 ay sonra kontrol'),
(37, 4, NULL, 1, '2025-11-17', 'Boğaz kültürü: Beta hemolitik streptokok', 'Penisilin 4x1, Parasetamol', '10 günlük antibiyotik tedavisi'),
(38, 7, NULL, 1, '2025-11-17', 'Psikiyatrik değerlendirme: İyileşme', 'Sertraline doz azaltımı', '2 hafta sonra kontrole çağrıldı'),
(39, 10, NULL, 2, '2025-11-18', 'USG: Tendinit düzelme', 'Fizik tedavi devam', 'Haftada 3 kez egzersiz programı'),
(40, 15, NULL, 3, '2025-11-18', 'Psikiyatrik değerlendirme: Stabil', 'Terapi devam', 'Aylık kontrollerle devam'),

-- 41-50. kayıtlar
(41, 17, NULL, 6, '2025-11-19', 'MR: Lomber spondiloz ilerlemesi', 'Fizik tedavi, Gabapentin 300mg', 'Ağrı yönetimi, postür egzersizleri'),
(42, 19, NULL, 12, '2025-11-19', 'EKG: Atrial fibrilasyon', 'Warfarin 5mg, Digoksin', 'INR takibi, kardiyoloji takibi'),
(43, 21, NULL, 17, '2025-11-20', 'Röntgen: Omurga eğriliği', 'Fizik tedavi, Korse', 'Duruş düzeltme egzersizleri'),
(44, 23, NULL, 22, '2025-11-20', 'Kan testleri: Demir eksikliği anemisi', 'Demir supplementi, Vitamin C', '3 ay sonra kan sayımı kontrolü'),
(45, 25, NULL, 3, '2025-11-21', 'USG: Safra kesesi polipi', 'Diyet önerileri', '6 ay sonra USG kontrolü'),
(46, 27, NULL, 18, '2025-11-21', 'CRP: 8 mg/L, Sedim: 25 mm/saat', 'NSAID ihtiyaç halinde', 'Romatolojik takip devam'),
(47, 29, NULL, 25, '2025-11-22', 'Psikiyatrik değerlendirme: İyileşme', 'Sertraline 25mg 1x1', 'Doz azaltımı, 1 ay sonra kontrol'),
(48, 2, NULL, 1, '2025-11-22', 'MR: Menisküs iyileşme', 'Fizik tedavi tamamlandı', 'Spor aktivitelerine kademeli dönüş'),
(49, 4, NULL, 1, '2025-11-23', 'Boğaz kültürü: Normal flora', 'Öneri yok', 'Enfeksiyon tamamen iyileşti'),
(50, 6, NULL, 1, '2025-11-23', 'Göz muayenesi: Görme keskinliği 1.0', 'Gözlük reçetesi güncellendi', 'Yıllık rutin kontrol önerildi');