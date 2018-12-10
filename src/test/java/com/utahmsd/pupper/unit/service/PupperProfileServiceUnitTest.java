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
//            given(userProfileRepo.save(any(UserProfile.class))).willReturn(userProfile);
//
//            userProfileService.createOrUpdateUserProfile(userProfile);
//
//            verify(userProfileRepo, times(1)).findByUserAccount_Username(anyString());
//            verify(userProfileRepo, times(1)).save(any(UserProfile.class));
    }

    @Test
    public void testUpdatePupperProfileByPupperProfileId() {
//            userProfile.setId(3L);
//
//            given(userProfileRepo.save(any(UserProfile.class))).willReturn(userProfile);
//            given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));
//
//            userProfileService.updateUserProfileByUserProfileId(userProfile.getId(), userProfile);
//
//            verify(userProfileRepo, times(1)).findById(anyLong());
//            verify(userProfileRepo, times(1)).save(any(UserProfile.class));
    }

    @Test
    public void testUpdateUserProfileByPupperProfileId_idsMismatch() {
//            userProfile.setId(3L);
//
//            given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));
//
//            userProfileService.updateUserProfileByUserProfileId(4L, userProfile);
//
//            verify(userProfileRepo, times(1)).findById(anyLong());
//            verify(userProfileRepo, times(0)).save(any(UserProfile.class));
    }

    @Test
    public void testDeletePupperProfileById_invalidId() {
//            given(userProfileRepo.findById(anyLong())).willReturn(Optional.empty());
//
//            userProfileService.deleteUserProfileById(anyLong());
//
//            verify(userProfileRepo).findById(anyLong());
//            verify(userProfileRepo, times(0)).deleteById(anyLong());
    }

}
