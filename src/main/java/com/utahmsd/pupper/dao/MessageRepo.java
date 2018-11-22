package com.utahmsd.pupper.dao;

import com.utahmsd.pupper.dao.entity.PupperMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo extends PagingAndSortingRepository<PupperMessage, Long> {
    Iterable<PupperMessage> findAll();
    Page<PupperMessage> findAll(Sort sort);
    Optional<List<PupperMessage>> findAllByMatchProfileSender_IdOrMatchProfileReceiver_Id(Long matchProfileId1, Long matchProfileId2);
    Optional<List<PupperMessage>> findAllByMatchProfileSender_IdAndMatchProfileReceiver_Id(Long matchProfileId1, Long matchProfileId2);

    PupperMessage save(PupperMessage pupperMessage);
    void deleteAllByMatchProfileSender_IdOrMatchProfileReceiver_Id(Long matchProfileSenderId, Long matchProfileReceiverId);

}
