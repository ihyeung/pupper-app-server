package com.utahmsd.pupper.unit.service;

import com.utahmsd.pupper.TestUtils;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.Breed;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.MatchProfileResponse;
import com.utahmsd.pupper.service.MatchProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

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
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("matchProfileServiceUnitTest" + UUID.randomUUID().toString().substring(24) + "@gmail.com");

        userProfile = TestUtils.createUserProfile(userAccount);
        matchProfile = TestUtils.createMatchProfile(userProfile);

        given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));
        given(matchProfileRepo.save(any(MatchProfile.class))).willReturn(matchProfile);

    }

    @Test
    public void testFindMatchProfileById_success() {
        given(matchProfileRepo.findById(anyLong())).willReturn(Optional.of(matchProfile));

        MatchProfile profile = matchProfileService.getMatchProfileByMatchProfileId(1L);

        verify(matchProfileRepo).findById(anyLong());
        assertEquals(matchProfile, profile);
    }
    @Test
    public void testFindMatchProfileById_invalidId() {
        given(matchProfileRepo.findById(anyLong())).willReturn(Optional.empty());

        MatchProfile matchProfile = matchProfileService.getMatchProfileByMatchProfileId(1L);

        verify(matchProfileRepo).findById(anyLong());
        assertNull(matchProfile);
    }

    @Test
    public void testCreateMatchProfile_noMatchProfilesForUser_success() {
        when(matchProfileRepo.findAllByUserProfile_Id(anyLong())).thenReturn(Optional.of(Collections.emptyList()));

        matchProfile.getUserProfile().setId(1L);
        MatchProfileResponse response = matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verify(userProfileRepo).findById(anyLong());

        verify(matchProfileRepo, times(1)).findAllByUserProfile_Id(anyLong());
        verify(matchProfileRepo, times(1)).save(any(MatchProfile.class));

        assertEquals(Arrays.asList(matchProfile), response.getMatchProfileList());
    }

    @Test
    public void testCreateMatchProfile_otherMatchProfilesExistForUser_updateIsDefaultMatchProfile_success() {
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
    public void testCreateMatchProfile_creatingDuplicateMatchProfile_failure() {
        when(matchProfileRepo.findAllByUserProfile_Id(anyLong())).thenReturn(Optional.of(Arrays.asList(matchProfile)));

        matchProfile.getUserProfile().setId(1L);
        MatchProfileResponse response = matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verify(userProfileRepo).findById(anyLong());
        verify(matchProfileRepo, times(1)).findAllByUserProfile_Id(anyLong());
        verify(matchProfileRepo, times(0)).save(any(MatchProfile.class));

        assertFalse(response.isSuccess());
    }

    @Test
    public void testCreateMatchProfile_idsMismatch() {
        matchProfile.getUserProfile().setId(10L);

        matchProfileService.createMatchProfileForUser(1L, matchProfile);

        verifyZeroInteractions(matchProfileRepo);
    }
}