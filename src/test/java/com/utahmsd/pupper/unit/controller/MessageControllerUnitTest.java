package com.utahmsd.pupper.unit.controller;

import com.utahmsd.pupper.controller.MessageController;
import com.utahmsd.pupper.service.MessageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MessageControllerUnitTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;


}