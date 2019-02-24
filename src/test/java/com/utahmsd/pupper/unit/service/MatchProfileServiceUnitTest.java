package com.utahmsd.pupper.unit.service;

import com.utahmsd.pupper.TestUtils;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MatchProfileServiceUnitTest {

    @Mock
    private MatchProfileRepo matchProfileRepo;

    @Mock
    private UserProfileRepo userProfileRepo;

    @InjectMocks
    private MatchProfileService matchProfileService;

    private MatchProfile matchProfile;
    private UserProfile userProfile;

    @Before
    public void init() {

        userProfile = TestUtils.createUserProfile(TestUtils.createUserAccount());
        matchProfile = TestUtils.createMatchProfile(userProfile);

        given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));
        given(matchProfileRepo.save(any(MatchProfile.class))).willReturn(matchProfile);

    }

    @Test
    public void testGetMatchProfileByMatchProfileId_success() {
        given(matchProfileRepo.findById(anyLong())).willReturn(Optional.of(matchProfile));

        MatchProfile profile = matchProfileService.getMatchProfileByMatchProfileId(1L);

        verify(matchProfileRepo).findById(anyLong());
        assertEquals(matchProfile, profile);
    }
    @Test
    public void testGetMatchProfileByMatchProfileId_invalidId() {
        given(matchProfileRepo.findById(anyLong())).willReturn(Optional.empty());

        MatchProfile matchProfile = matchProfileService.getMatchProfileByMatchProfileId(1L);

        verify(matchProfileRepo).findById(anyLong());
        assertNull(matchProfile);
    }

    @Test
    public void testCreateMatchProfileForUser_noMatchProfilesForUser_success() {
        when(matchProfileRepo.findAllByUserProfile_Id(anyLong())).thenReturn(Optional.of(Collections.emptyList()));

        matchProfile.getUserProfile().setId(1L);
        MatchProfileResponse response = matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verify(userProfileRepo).findById(anyLong());

        verify(matchProfileRepo, times(1)).findAllByUserProfile_Id(anyLong());
        verify(matchProfileRepo, times(1)).save(any(MatchProfile.class));

        assertEquals(Arrays.asList(matchProfile), response.getMatchProfileList());
    }

    @Test
    public void testCreateMatchProfileForUser_otherMatchProfilesExistForUser_updateIsDefaultMatchProfile_success() {
        MatchProfile existingMatchProfile = TestUtils.createMatchProfile(userProfile);
        existingMatchProfile.setNames("Rocco");
        Breed breed = new Breed();
        breed.setName("Golden Retriever");
        existingMatchProfile.setBreed(breed);

        when(matchProfileRepo.findAllByUserProfile_Id(anyLong()))
                .thenReturn(Optional.of(Arrays.asList(existingMatchProfile)));

        matchProfile.getUserProfile().setId(1L);

        MatchProfileResponse response = matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verify(userProfileRepo).findById(anyLong());

        verify(matchProfileRepo, times(2)).save(any(MatchProfile.class));
        verify(matchProfileRepo, times(1)).findAllByUserProfile_Id(anyLong());
        assertEquals(Arrays.asList(matchProfile), response.getMatchProfileList());
    }

    @Test
    public void testCreateMatchProfileForUser_creatingDuplicateMatchProfile_failure() {
        when(matchProfileRepo.findAllByUserProfile_Id(anyLong())).thenReturn(Optional.of(Arrays.asList(matchProfile)));

        matchProfile.getUserProfile().setId(1L);
        MatchProfileResponse response = matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verify(userProfileRepo).findById(anyLong());
        verify(matchProfileRepo, times(1)).findAllByUserProfile_Id(anyLong());
        verify(matchProfileRepo, times(0)).save(any(MatchProfile.class));

        assertFalse(response.isSuccess());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testCreateMatchProfileForUser_idsMismatch() {
        matchProfile.getUserProfile().setId(10L);

        MatchProfileResponse response = matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verifyZeroInteractions(matchProfileRepo);
        assertFalse(response.isSuccess());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }
}