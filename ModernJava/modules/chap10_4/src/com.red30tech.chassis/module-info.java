module com.red30tech.chassis {
   requires com.red30tech.axle;
   exports com.red30tech.chassis.api;
   provides com.red30tech.chassis.api.Chassis with
            com.red30tech.chassis.type.SuvChassis;
}
