# 나의 취향 페이지 - 수행평가 시작 저장소

멀티패스 서버 하나에 Spring Boot(백엔드) + 화면(프론트) + MySQL(데이터베이스)을 올리고,
브라우저에서 `http://멀티패스IP:8080` 으로 확인한다.

## 파일이 하는 일
- `src/main/resources/static/index.html` : 내 취향 페이지 화면 (내가 꾸민다)
- `src/main/java/com/example/linuxspringboot/FavoriteController.java` : 백엔드 기능 (내가 기능을 추가한다)
- `src/main/resources/application.properties` : DB 연결 정보 (비밀번호를 넣는다)
- `db/schema.sql` : 취향 표를 만드는 SQL

## 1. MySQL 준비
```
sudo apt update
sudo apt install -y mysql-server
sudo mysql
```
mysql> 안에서 (비밀번호는 내가 정한다)
```
CREATE DATABASE appdb;
CREATE USER 'appuser'@'localhost' IDENTIFIED BY '내비밀번호';
GRANT ALL PRIVILEGES ON appdb.* TO 'appuser'@'localhost';
EXIT;
```

## 2. 저장소 받기와 표 만들기
```
cd ~
git clone https://github.com/ksss0926-lang/yes.git
cd yes
mysql -u appuser -p appdb < db/schema.sql
mysql -u appuser -p appdb -e "SELECT * FROM favorites;"
```

## 3. DB 비밀번호 넣기
```
vi src/main/resources/application.properties
```
`CHANGE_ME` 를 1단계에서 정한 비밀번호로 바꾼다.

## 4. 빌드하고 실행하기
```
sudo apt install -y openjdk-21-jdk
java -version              # 21 이 보여야 한다
chmod +x gradlew
./gradlew clean build
nohup java -jar build/libs/linux-springboot-0.0.1-SNAPSHOT.jar >> app.log 2>&1 &
```

## 5. 확인하기
```
curl localhost:8080/api/hello
curl localhost:8080/api/favorites
```
내 컴퓨터 브라우저에서 `http://멀티패스IP:8080` 을 연다. (IP 는 내 컴퓨터에서 `multipass list`)

## 6. 고친 뒤 다시 올리기 (화면이나 기능을 고칠 때마다)
화면 파일도 jar 안에 들어가므로, 고친 뒤에는 꼭 다시 빌드하고 다시 실행한다.
```
ps aux | grep java          # 돌고 있는 서버의 PID 확인
kill PID번호                # 이전 서버 끄기
./gradlew clean build
nohup java -jar build/libs/linux-springboot-0.0.1-SNAPSHOT.jar >> app.log 2>&1 &
```

## 7. 내가 정하는 기능 추가하기 (매번 같은 3단계)
1. DB : 필요하면 칸을 추가한다 (예: `ALTER TABLE favorites ADD likes INT DEFAULT 0;`)
2. 백엔드 : `FavoriteController.java` 에 주소 하나를 추가한다 (파일 아래쪽 예시 참고)
3. 화면 : `index.html` 에 버튼을 하나 추가하고 `fetch('/api/...')` 로 부른다

## 막힐 때
- 서버가 바로 꺼진다 → `tail -n 50 app.log` 에서 ERROR 를 읽는다
  - `Access denied` : application.properties 의 비밀번호 확인
  - `Communications link failure` : `systemctl status mysql` 로 MySQL 이 켜져 있는지 확인
- `Port 8080 was already in use` → `sudo lsof -i:8080` 으로 PID 를 찾아 `kill`
- `./gradlew: Permission denied` → `chmod +x gradlew`
- `Cannot find a Java installation ... languageVersion=21` → `sudo apt install -y openjdk-21-jdk`
- `UnsupportedClassVersionError` → 실행하는 자바가 21이 아니다. `sudo update-alternatives --config java` 에서 21을 고른다
- 화면은 뜨는데 목록이 비어 있다 → `curl localhost:8080/api/favorites` 가 되는지 먼저 본다
