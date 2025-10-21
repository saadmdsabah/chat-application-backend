package com.saadsabahuddin.chat_application_backend.service;

import com.saadsabahuddin.chat_application_backend.dto.CreateInvitationDto;
import com.saadsabahuddin.chat_application_backend.dto.UpdateInvitationStatusDto;
import com.saadsabahuddin.chat_application_backend.entities.Invitation;
import com.saadsabahuddin.chat_application_backend.entities.User;
import com.saadsabahuddin.chat_application_backend.repository.InvitationRepository;
import com.saadsabahuddin.chat_application_backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {

  private final InvitationRepository invitationRepository;
  private final UserRepository userRepository;

  @Caching(
    evict = {
      @CacheEvict(
        value = "invitationsBySender",
        key = "#createInvitationDto.getSender()"
      ),
      @CacheEvict(
        value = "invitationsByReceiver",
        key = "#createInvitationDto.getReceiver()"
      ),
    }
  )
  public Invitation createInvitation(CreateInvitationDto createInvitationDto) {
    if (
      createInvitationDto.getReceiver().equals(createInvitationDto.getSender())
    ) {
      throw new IllegalArgumentException(
        "Sender and receiver cannot be the same"
      );
    }

    userRepository
      .findByUserName(createInvitationDto.getReceiver())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    userRepository
      .findByUserName(createInvitationDto.getSender())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (
      invitationRepository
        .findByReceiverAndSender(
          createInvitationDto.getReceiver(),
          createInvitationDto.getSender()
        )
        .isPresent()
    ) {
      throw new IllegalArgumentException("Invitation already exists");
    }
    return invitationRepository.save(
      Invitation
        .builder()
        .createdAt(LocalDateTime.now())
        .receiver(createInvitationDto.getReceiver())
        .sender(createInvitationDto.getSender())
        .roomId(createInvitationDto.getRoomId())
        .status("pending")
        .build()
    );
  }

  @Caching(
    evict = {
      @CacheEvict(
        value = "invitationsBySender",
        key = "#updateInvitationStatusDto.getSender()"
      ),
      @CacheEvict(
        value = "invitationsByReceiver",
        key = "#updateInvitationStatusDto.getReceiver()"
      ),
      @CacheEvict(
        value = "checkIfEntryPossible",
        key = "#updateInvitationStatusDto.getRoomId() + '_' + #updateInvitationStatusDto.getSender()"
      ),
      @CacheEvict(
        value = "joinedRooms",
        key = "#updateInvitationStatusDto.getSender()"
      ),
    }
  )
  public Boolean updateStatusOfInvitation(
    UpdateInvitationStatusDto updateInvitationStatusDto
  ) {
    if (updateInvitationStatusDto.getStatus().equals("accepted")) {
      // add room to user joined rooms
      invitationRepository
        .findByReceiverAndSender(
          updateInvitationStatusDto.getReceiver(),
          updateInvitationStatusDto.getSender()
        )
        .orElseThrow(() ->
          new IllegalArgumentException("Invitation does not exist")
        );
      User sender = userRepository
        .findByUserName(updateInvitationStatusDto.getSender())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      sender.getJoinedRooms().add(updateInvitationStatusDto.getRoomId());
      userRepository.save(sender);
      invitationRepository.deleteByReceiverAndSender(
        updateInvitationStatusDto.getReceiver(),
        updateInvitationStatusDto.getSender()
      );
      return true;
    }
    invitationRepository
      .findByReceiverAndSender(
        updateInvitationStatusDto.getReceiver(),
        updateInvitationStatusDto.getSender()
      )
      .orElseThrow(() ->
        new IllegalArgumentException("Invitation does not exist")
      );
    invitationRepository.deleteByReceiverAndSender(
      updateInvitationStatusDto.getReceiver(),
      updateInvitationStatusDto.getSender()
    );
    return false;
  }

  @Cacheable(value = "invitationsBySender", key = "#userName")
  public List<Invitation> getAllInvitationsForASender(String userName) {
    return invitationRepository
      .findBySender(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Cacheable(value = "invitationsByReceiver", key = "#userName")
  public List<Invitation> getAllInvitationsForAReciever(String userName) {
    return invitationRepository
      .findByReceiver(userName)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
