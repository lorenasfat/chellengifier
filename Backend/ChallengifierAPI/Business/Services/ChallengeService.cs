﻿using Business.DTOs;
using Business.Mappers;
using Business.Services.Interfaces;
using DataAccess.Entities;
using DataAccess.UnitOfWork;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Business.Services
{
    public class ChallengeService : IChallengeService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IObjectiveService _objectiveService;

        public ChallengeService(IUnitOfWork unitOfWork, IObjectiveService objectiveService)
        {
            _unitOfWork = unitOfWork;
            _objectiveService = objectiveService;
        }
        public void AddChallenge(ChallengeDto challenge)
        {
            try
            {
                _unitOfWork.ChallengeRepository.Create(challenge.ToDbEntity());
                _unitOfWork.ChallengeRepository.Save();
                _unitOfWork.Commit();
            }
            catch (Exception)
            {
                _unitOfWork.RollBack();
                throw;
            }
        }

        public void DeleteChallenge(Guid challengeId)
        {
            try
            {
                var dbChalenge = _unitOfWork.ChallengeRepository.GetById(challengeId);
                if (dbChalenge != null)
                {
                    _unitOfWork.ChallengeRepository.Delete(dbChalenge);
                    _unitOfWork.ChallengeRepository.Save();
                    _unitOfWork.Commit();
                }
            }
            catch (Exception)
            {
                _unitOfWork.RollBack();
                throw;
            }
        }

        public void Dispose()
        {
            _unitOfWork.Dispose();
        }

        public IEnumerable<ChallengeDto> GetAllChallenges()
        {
            var challenges = _unitOfWork.ChallengeRepository.All().Where(c => !c.Archived);
            return challenges.ToDtos();
        }

        public IEnumerable<MyChallengeDto> GetChallengesOfUser(string id)
        {
            var challenges = _unitOfWork.ChallengeRepository.All().Where(c => c.User_ID == id && c.Archived == false);
            var userChallenges = challenges.ToMyDtos();

            foreach (MyChallengeDto challenge in userChallenges)
            {
                challenge.Acceptance = _objectiveService.CountForChallenge(challenge.Id);
            }

            return userChallenges;
        }

        public ChallengeDto GetChallengeById(Guid challengeId)
        {
            return _unitOfWork.ChallengeRepository.GetById(challengeId).ToDto();
        }

        public ChallengeDto GetChallengeByName(string name)
        {
            return _unitOfWork.ChallengeRepository.All().FirstOrDefault(c => c.Name.ToUpper().Contains(name.ToUpper())).ToDto();
        }

        public void UpdateChallenge(ChallengeDto challenge)
        {
            try
            {
                var dbChallenge = _unitOfWork.ChallengeRepository.GetById(challenge.Id);
                if (dbChallenge == null)
                {
                    AddChallenge(challenge);
                }
                else
                {
                    SetUpChallenge(challenge, dbChallenge);
                    _unitOfWork.ChallengeRepository.Save();
                    _unitOfWork.Commit();
                }
            }
            catch (Exception ex)
            {
                _unitOfWork.RollBack();
                throw;
            }
        }

        private void SetUpChallenge(ChallengeDto challenge, Challenge dbChallenge)
        {
            dbChallenge.Description = challenge.Description;
            dbChallenge.Name = challenge.Name;
            dbChallenge.Suggested_Time_UnitsId = challenge.Suggested_Time_UnitsId;
            dbChallenge.Suggested_Time_Number = challenge.Suggested_Time_Number;
            dbChallenge.User_ID = challenge.User_Id;
        }

        public int CountObjectivesForReview(Guid id)
        {
            var nr = _unitOfWork.ObjectiveRepository.All().Where(o => o.Status_ID == (int)Common.Enums.ObjectiveStatus.ForReview && o.Challenge_ID == id).Count();
            return nr;
        }

        public IEnumerable<ArchivedChallengeDto> GetArchivedChallengesOfUser(string id)
        {
            var challenges = _unitOfWork.ChallengeRepository.All().Where(c => c.Archived && c.User_ID.CompareTo(id) == 0);
            var dtos = challenges.ToArchivedChallenges();

            foreach (ArchivedChallengeDto challenge in dtos)
            {
                challenge.AcceptedBy = _unitOfWork.ObjectiveRepository.All().Count(o => o.Challenge_ID == challenge.Id);
            }

            return dtos;
        }

        public void ArchiveChallenge(Guid challengeId)
        {
            try
            {
                _unitOfWork.ChallengeRepository.All().Where(c => c.Challenge_ID == challengeId).FirstOrDefault().Archived = true;
                _unitOfWork.ChallengeRepository.Save();
                _unitOfWork.Commit();
            }
            catch (Exception ex)
            {
                _unitOfWork.RollBack();
                throw;
            }
        }
    }
}
