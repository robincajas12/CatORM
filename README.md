
# How to use

## 1. Configuración Inicial

Establece la conexión y construye la base de datos:

```java
Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "user", "password");
MyAppDatabase db = IppodakeORM.buildDataBase(MyAppDatabase.class, connection, false);
```

## 2. Definir Entidades

```java
@Entity("users")
public class User {
    @PrimaryKey(autoIncrement = true)
    @ColumnInfo(name = "id")
    private Long id;
    
    @ColumnInfo(name = "name")
    private String name;
    
    // Constructor vacío y getters/setters
}
```

## 3. Crear DAOs

```java
@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE name = :name")
    List<User> findByName(@P("name") String name);
    
    @Insert
    boolean insert(User user);
    
    @Update
    boolean update(User user);
    
    @Delete
    boolean delete(User user);
}
```

## 4. Configurar Base de Datos

```java
@Database(entities = {User.class, Post.class})
public interface MyAppDatabase {
    UserDao userDao();
    PostDao postDao();
}
```

## 5. Usar el ORM

```java
UserDao userDao = db.userDao();
User user = new User();
user.setName("Juan");
userDao.insert(user);
List<User> users = userDao.findByName("Juan");
```

## 6. Entidades para Consultas Multitabla

Crea entidades DTO que no se agregan a `@Database.entities`:

```java
@Entity("user_posts_view") // No se crea tabla física
public class UserWithPosts {
    @ColumnInfo(name = "user_id")
    private Long userId;
    
    @ColumnInfo(name = "user_name")
    private String userName;
    
    @ColumnInfo(name = "post_title")
    private String postTitle;
    
    public UserWithPosts() {}
}
```

DAO con consultas multitabla:

```java
@Dao
public interface UserPostsDao {
    @Query("SELECT u.id as user_id, u.name as user_name, p.title as post_title " +
           "FROM users u LEFT JOIN posts p ON u.id = p.user_id " +
           "WHERE u.id = :userId")
    List<UserWithPosts> getUserWithPosts(@P("userId") Long userId);
}
```

Configuración:

```java
@Database(entities = {User.class, Post.class}) // Solo entidades reales
public interface MyAppDatabase {
    UserDao userDao();
    PostDao postDao();
    UserPostsDao userPostsDao(); // DAO para consultas multitabla
}
```

El sistema mapea automáticamente las columnas del resultado usando reflection.

## Notes

Las entidades DTO permiten consultas complejas sin crear tablas físicas. El mapeo funciona igual que con entidades normales, usando `@ColumnInfo` para mapear columnas a campos. Los nombres de columnas en el SQL deben coincidir exactamente con los valores en las anotaciones.
