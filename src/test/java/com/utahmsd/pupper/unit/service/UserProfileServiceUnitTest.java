package com.utahmsd.pupper.unit.service;

import com.utahmsd.pupper.TestUtils;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.service.UserProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileServiceUnitTest {

    @Mock
    private UserProfileRepo userProfileRepo;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfile userProfile;

    @Before
    public void init() {
        UserAccount userAccount = TestUtils.createUserAccount();

        userProfile = TestUtils.createUserProfile(userAccount);
    }

    @Test
    public void testFindUserProfileById() {
        given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));

        userProfileService.findUserProfileById(1L);

        verify(userProfileRepo).findById(anyLong());
    }

    @Test
    public void testCreateOrUpdateUserProfile() {
        given(userProfileRepo.findByUserAccount_Username(anyString())).willReturn(userProfile);
        given(userProfileRepo.save(any(UserProfile.class))).willReturn(userProfile);

        userProfileService.createOrUpdateUserProfile(userProfile);

        verify(userProfileRepo, times(1)).findByUserAccount_Username(anyString());
        verify(userProfileRepo, times(1)).save(any(UserProfile.class));
    }

    @Test
    public void testUpdateUserProfileByUserProfileId() {
        userProfile.setId(3L);

        given(userProfileRepo.save(any(UserProfile.class))).willReturn(userProfile);
        given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));

        userProfileService.updateUserProfileByUserProfileId(userProfile.getId(), userProfile);

        verify(userProfileRepo, times(1)).findById(anyLong());
        verify(userProfileRepo, times(1)).save(any(UserProfile.class));
    }

    @Test
    public void testUpdateUserProfileByUserProfileId_idsMismatch() {
        userProfile.setId(3L);

        given(userProfileRepo.findById(anyLong())).willReturn(Optional.of(userProfile));

        userProfileService.updateUserProfileByUserProfileId(4L, userProfile);

        verify(userProfileRepo, times(1)).findById(anyLong());
        verify(userProfileRepo, times(0)).save(any(UserProfile.class));
    }

    @Test
    public void testUpdatLastLoginForUserProfile_invalidLastLoginString() {
        userProfileService.updateLastLoginForUserProfile(userProfile.getId(), "INVALID LOGIN TIMESTAMP");

        verifyZeroInteractions(userProfileRepo);
    }

    @Test
    public void testDeleteUserProfileById_invalidId() {
        given(userProfileRepo.findById(anyLong())).willReturn(Optional.empty());

        userProfileService.deleteUserProfileById(anyLong());

        verify(userProfileRepo).findById(anyLong());
        verify(userProfileRepo, times(0)).deleteById(anyLong());
    }

}
