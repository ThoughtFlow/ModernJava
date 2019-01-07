module com.red30tech.chassis {
   requires com.red30tech.axle;
   requires static com.red30tech.airbag;
   exports com.red30tech.chassis.api;
   provides com.red30tech.chassis.api.Chassis with com.red30tech.chassis.type.SuvChassis, com.red30tech.chassis.type.SedanChassis;
}
