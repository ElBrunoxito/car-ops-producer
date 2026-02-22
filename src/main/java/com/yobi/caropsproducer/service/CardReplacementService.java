package com.yobi.caropsproducer.service;

import com.yobi.caropsproducer.dto.CardReplacementRequestDTO;
import io.reactivex.rxjava3.core.Single;

public interface CardReplacementService {
    Single<String> process(CardReplacementRequestDTO dto);

}
