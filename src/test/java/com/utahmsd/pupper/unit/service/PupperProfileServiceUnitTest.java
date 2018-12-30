package com.utahmsd.pupper.unit.service;

import com.utahmsd.pupper.TestUtils;
import com.utahmsd.pupper.dao.BreedRepo;
import com.utahmsd.pupper.dao.MatchProfileRepo;
import com.utahmsd.pupper.dao.PupperProfileRepo;
import com.utahmsd.pupper.dao.entity.MatchProfile;
import com.utahmsd.pupper.dao.entity.PupperProfile;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.service.PupperProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PupperProfileServiceUnitTest {

    @Mock
    private PupperProfileRepo pupperProfileRepo;

    @InjectMocks
    private PupperProfileService pupperProfileService;

    @Mock
    private MatchProfileRepo matchProfileRepo;

    @Mock
    private BreedRepo breedRepo;

    private MatchProfile matchProfile;
    private PupperProfile pupperProfile;

    @Before
    public void init() {
        UserAccount userAccount = TestUtils.createUserAccount();
        UserProfile userProfile = TestUtils.createUserProfile(userAccount);
        matchProfile = TestUtils.createMatchProfile(userProfile);
        pupperProfile = TestUtils.createPupperProfile(matchProfile);


    }

    @Test
    public void testFindPupperProfileById() {
        given(pupperProfileRepo.findById(anyLong())).willReturn(Optional.of(pupperProfile));

        pupperProfileService.findPupperProfileById(1L,2L,3L);

        verify(pupperProfileRepo).findById(anyLong());
    }

    @Test
    public void testFindPupperProfileById_invalidPupperProfileId() {
        given(pupperProfileRepo.findById(anyLong())).willReturn(Optional.empty());

        pupperProfileService.findPupperProfileById(1L,2L,3L);

        verify(pupperProfileRepo).findById(anyLong());
    }

    @Test
    public void testCreateOrUpdatePupperProfile() {
    }

    @Test
    public void testUpdatePupperProfileByPupperProfileId() {
    }

    @Test
    public void testUpdateUserProfileByPupperProfileId_idsMismatch() {
    }

    @Test
    public void testDeletePupperProfileById_invalidId() {
    }
}
