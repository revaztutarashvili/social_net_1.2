package com.socialplatformapi.service;


import com.socialplatformapi.client.ReqresClient;
import com.socialplatformapi.dto.external.ReqresResponse;
import com.socialplatformapi.dto.external.ReqresUser;
import com.socialplatformapi.model.User;
import com.socialplatformapi.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReqresService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReqresClient reqresClient;

    @Value("${reqres.api.key:reqres-free-v1}")
    private String reqresApiKey;

    private final List<Long> reqresUserIds = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void fetchAndSaveReqresUsers() {
        try {
            log.info("Fetching users from reqres.in...");

            // პირველი გვერდის მოთხოვნა
            ReqresResponse response = reqresClient.getUsers(reqresApiKey, null);

            if (response != null && response.getData() != null) {
                saveReqresUsers(response.getData());

                // დარჩენილი გვერდების მოთხოვნა
                for (int page = 2; page <= response.getTotalPages(); page++) {
                    ReqresResponse pageResponse = reqresClient.getUsers(reqresApiKey, page);

                    if (pageResponse != null && pageResponse.getData() != null) {
                        saveReqresUsers(pageResponse.getData());
                    }
                }

                log.info("Successfully imported {} users from reqres.in", reqresUserIds.size());
            }

        } catch (Exception e) {
            log.error("Error fetching users from reqres.in: {}", e.getMessage(), e);
        }
    }

    private void saveReqresUsers(List<ReqresUser> reqresUsers) {
        for (ReqresUser reqresUser : reqresUsers) {
            try {
                // რექრესიდან წამოღებულ იუზერებს შევინახავ ჩემი მოთხოვნების და ქონსთრეინთების მიხედვით
                if (!userRepository.existsByEmail(reqresUser.getEmail())) {
                    User user = new User();
                    user.setFirstName(reqresUser.getFirstName())
                            .setLastName(reqresUser.getLastName())
                            .setUsername(reqresUser.getEmail()) //როგორც იყო მოთხოვნაში
                            .setEmail(reqresUser.getEmail())
                            .setBirthDate(LocalDate.now().minusYears(18)) //დროებითი birthdate;
                            .setPassword(passwordEncoder.encode("jgufuridavaleba6")); // დროებითი პაროლი

                    userRepository.save(user);
                    reqresUserIds.add(user.getId());

                    log.debug("Saved reqres user: {}", reqresUser.getEmail());
                }
            } catch (Exception e) {
                log.error("Error saving reqres user {}: {}", reqresUser.getEmail(), e.getMessage());
            }
        }
    }

    @PreDestroy
    public void cleanupReqresUsers() {
        try {
            if (!reqresUserIds.isEmpty()) {
                log.info("Cleaning up {} reqres users...", reqresUserIds.size());
                userRepository.deleteAllById(reqresUserIds);
                log.info("Successfully cleaned up reqres users");
            }
        } catch (Exception e) {
            log.error("Error cleaning up reqres users: {}", e.getMessage(), e);
        }
    }
}