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
    
    public partial class Objective
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Objective()
        {
            this.Milestone = new HashSet<Milestone>();
            this.UserRating = new HashSet<UserRating>();
            this.Picture = new HashSet<Picture>();
        }
    
        public System.Guid Objective_ID { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public System.DateTime Deadline { get; set; }
        public string Expected_Outcome { get; set; }
        public Nullable<System.Guid> Challenge_ID { get; set; }
        public int Status_ID { get; set; }
        public double Rating { get; set; }
        public Nullable<System.DateTime> Start_Date { get; set; }
        public Nullable<System.DateTime> End_Date { get; set; }
        public string User_ID { get; set; }
    
        public virtual AspNetUsers AspNetUsers { get; set; }
        public virtual Challenge Challenge { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Milestone> Milestone { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<UserRating> UserRating { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Picture> Picture { get; set; }
        public virtual ObjectiveStatus ObjectiveStatus { get; set; }
    }
}
