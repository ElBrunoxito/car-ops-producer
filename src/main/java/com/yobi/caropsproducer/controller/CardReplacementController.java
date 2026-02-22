package com.yobi.caropsproducer.controller;

import com.yobi.caropsproducer.dto.CardReplacementRequestDTO;
import com.yobi.caropsproducer.service.CardReplacementService;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("card-replacements")
@AllArgsConstructor
public class CardReplacementController {
    private final CardReplacementService cardReplacementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Single<String> create(@RequestBody @Valid CardReplacementRequestDTO dto) {
        return cardReplacementService.process(dto);
    }
}
