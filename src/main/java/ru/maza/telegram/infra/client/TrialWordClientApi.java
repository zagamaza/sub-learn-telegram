package ru.maza.telegram.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.dto.ResultDto;
import ru.maza.telegram.dto.ResultRequestDto;


@FeignClient(contextId = "results", name = "results", url = "localhost:8082/api/results")
public interface TrialWordClientApi {


    @GetMapping("/{id}")
    ResultDto get(@PathVariable("id") Long id);


    @GetMapping("/users/{id}")
    ResultDto getLastResultByUserId(@PathVariable("id") Long id);

    @PostMapping
    ResultDto save(@RequestBody ResultRequestDto resultRequestDto);

}
