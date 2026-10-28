package com.example.linuxspringboot;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 이 파일은 LinuxSpringbootApplication.java 와 같은 폴더에 있어야 Spring 이 찾을 수 있다.
@RestController
@RequestMapping("/api")
public class FavoriteController {

  private final JdbcTemplate db;   // SQL 을 그대로 실행해 주는 도구

  public FavoriteController(JdbcTemplate db) {
    this.db = db;
  }

  // 0. 연습용 - DB 없이 글자만 돌려준다. 서버가 살아 있는지 확인할 때 쓴다.
  //    확인: curl localhost:8080/api/hello
  @GetMapping("/hello")
  public String hello() {
    return "서버가 잘 돌고 있어요!";
  }

  // 1. [필수] 취향 목록 보기
  //    확인: curl localhost:8080/api/favorites
  @GetMapping("/favorites")
  public List<Map<String, Object>> list() {
    return db.queryForList(
        "SELECT id, name, reason, rating, created_at FROM favorites ORDER BY id DESC");
  }

  // 2. [필수] 취향 한 줄 추가 (화면의 추가 버튼이 부른다)
  @PostMapping("/favorites")
  public ResponseEntity<String> add(@RequestBody Map<String, Object> body) {
    Object name = body.get("name");
    if (name == null || name.toString().isBlank()) {
      return ResponseEntity.badRequest().body("name is empty");
    }
    db.update("INSERT INTO favorites (name, reason, rating) VALUES (?, ?, ?)",
        name, body.get("reason"), body.get("rating"));
    return ResponseEntity.ok("saved");
  }

  // 3. [내가 정하는 기능] 아래 예시 중 하나 이상을 골라 직접 만든다. (예시를 복사해 SQL 과 주소만 바꾸면 된다)
  //
  //  - 별점 높은 순 5개   @GetMapping("/favorites/top")         "SELECT ... ORDER BY rating DESC LIMIT 5"
  //  - 랜덤 추천 하나     @GetMapping("/favorites/random")      "SELECT ... ORDER BY RAND() LIMIT 1"
  //  - 이름으로 검색      @GetMapping("/favorites/search")      "SELECT ... WHERE name LIKE ?"  (+ @RequestParam)
  //  - 좋아요 +1          @PostMapping("/favorites/{id}/like")  "UPDATE favorites SET likes = likes + 1 WHERE id = ?"
  //                       (먼저 DB 에 칸 추가: ALTER TABLE favorites ADD likes INT DEFAULT 0;)
  //  - 삭제               @DeleteMapping("/favorites/{id}")     "DELETE FROM favorites WHERE id = ?"  (+ @PathVariable)
  //
  //  만든 뒤에는 index.html 에 버튼을 하나 추가해 fetch('/api/...') 로 부른다.
}
