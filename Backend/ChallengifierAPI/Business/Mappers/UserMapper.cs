﻿using Business.DTOs;
using DataAccess.Entities;

namespace Business.Mappers
{
    public static class UserMapper
    {
        public static User ToDbEntity(this UserDto user)
        {
            return new User()
            {
                User_ID = user.UserId,
                Username = user.Username,
                User_FirstName = user.FirstName,
                User_LastName = user.LastName
            };
        }
    }
}
