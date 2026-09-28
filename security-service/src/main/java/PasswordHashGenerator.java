
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder();

        String plainPassword = "admin@12345";

        String hashedPassword =
                passwordEncoder.encode(plainPassword);

        System.out.println("Plain password: " + plainPassword);
        System.out.println("BCrypt hash: " + hashedPassword);
    }
}
