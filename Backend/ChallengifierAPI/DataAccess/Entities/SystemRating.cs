//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace DataAccess.Entities
{
    using System;
    using System.Collections.Generic;
    
    public partial class SystemRating
    {
        public System.Guid Id { get; set; }
        public System.Guid ObjectiveId { get; set; }
        public int System_Grade { get; set; }
    
        public virtual Objective Objective { get; set; }
    }
}
