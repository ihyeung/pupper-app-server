package com.utahmsd.pupper.unit.service;

import com.utahmsd.pupper.TestUtils;
import com.utahmsd.pupper.dao.UserProfileRepo;
import com.utahmsd.pupper.dao.entity.UserAccount;
import com.utahmsd.pupper.dao.entity.UserProfile;
import com.utahmsd.pupper.dto.UserProfileResponse;
import com.utahmsd.pupper.service.UserProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
    public void testCreateUserProfile_success() {
        given(userProfileRepo.findByUserAccount_Username(anyString())).willReturn(null);
        given(userProfileRepo.save(any(UserProfile.class))).willReturn(userProfile);

        UserProfileResponse response = userProfileService.createUserProfile(userProfile);

        verify(userProfileRepo, times(1)).findByUserAccount_Username(anyString());
        verify(userProfileRepo, times(1)).save(any(UserProfile.class));
        assertEquals(Arrays.asList(userProfile), response.getUserProfileList());
    }

    @Test
    public void testCreateUserProfile_userProfileAlreadyExists() {
        given(userProfileRepo.findByUserAccount_Username(anyString())).willReturn(userProfile);

        UserProfileResponse response = userProfileService.createUserProfile(userProfile);

        verify(userProfileRepo, times(1)).findByUserAccount_Username(anyString());
        verify(userProfileRepo, times(0)).save(any(UserProfile.class));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertThat(response.getUserProfileList()).isEmpty();
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

        userProfileService.updateUserProfileByUserProfileId(4L, userProfile);

        verify(userProfileRepo, times(0)).findById(anyLong());
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
