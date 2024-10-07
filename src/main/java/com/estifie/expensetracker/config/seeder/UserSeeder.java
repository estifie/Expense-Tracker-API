package com.estifie.expensetracker.config.seeder;

import com.estifie.expensetracker.dto.auth.UserRegisterDTO;
import com.estifie.expensetracker.repository.UserRepository;
import com.estifie.expensetracker.service.AuthenticationService;
import com.estifie.expensetracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class UserSeeder implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(UserSeeder.class);
    private final Environment env;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    public UserSeeder(Environment env, AuthenticationService authenticationService, UserService userService, UserRepository userRepository) {
        this.env = env;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.getOptionValues("seeder") != null)
            if (args.getOptionValues("seeder")
                    .getFirst()
                    .equals("user")) {
                seedUser();
            }
    }

    private void seedUser() {
        String username = env.getProperty("seed.user.username");
        String password = env.getProperty("seed.user.password");
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(username, password);

        if (username == null || password == null) {
            logger.error("Seeding user failed. Username and password must be provided");
            return;
        }

        authenticationService.register(userRegisterDTO);

        try {
            userRepository.findByUsername(username);

            userService.grantPermission(username, "MANAGE_PERMISSIONS");
        } catch (Exception e) {
            logger.error("Seeding user failed. An error occurred while registering user");
            return;
        }

        logger.info("User seeded successfully");
        System.exit(0);
    }
}
