package telcoventas;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBcryptHashes {
    public static void main(String[] args) {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

        System.out.println("Admin*123 => " + enc.encode("Admin*123"));
        System.out.println("Agente*123 => " + enc.encode("Agente*123"));
        System.out.println("Back*123 => " + enc.encode("Back*123"));
        System.out.println("Sup*123 => " + enc.encode("Sup*123"));
    }
}