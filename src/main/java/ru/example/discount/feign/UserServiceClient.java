package ru.example.discount.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.example.discount.dto.UserTotalSpentDto;

@FeignClient(name = "user-service-client", url = "${user.service.url}")
public interface UserServiceClient {

    @GetMapping("/users/{id}")
    UserTotalSpentDto getUserTotalSpent(@PathVariable("id") Long userId);
}
