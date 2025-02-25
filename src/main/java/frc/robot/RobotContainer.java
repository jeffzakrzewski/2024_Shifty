// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.AutoConstants.AutoPattern;
import frc.robot.Constants.DriveConstants.DriveMode;
import frc.robot.commands.auto.AutonomousCommand;
import frc.robot.commands.drive.DefaultDriveCommand;
import frc.robot.operator.OperatorInput;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LightsSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    // The operator input class
    private final OperatorInput    operatorInput    = new OperatorInput();

    // The robot's subsystems and commands are defined here...
    private final DriveSubsystem   driveSubsystem   = new DriveSubsystem();
    private final LightsSubsystem  lightsSubsystem  = new LightsSubsystem();
    private final IntakeSubsystem  IntakeSubsystem  = new IntakeSubsystem(lightsSubsystem);
    private final ShooterSubsystem ShooterSubsystem = new ShooterSubsystem(lightsSubsystem);



    // All dashboard choosers are defined here...
    private final SendableChooser<DriveMode>   driveModeChooser   = new SendableChooser<>();
    private final SendableChooser<AutoPattern> autoPatternChooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {

        // Initialize all Subsystem default commands.
        driveSubsystem.setDefaultCommand(
            new DefaultDriveCommand(
                operatorInput.driverController, driveModeChooser,
                driveSubsystem));

        // Initialize the dashboard choosers
        initDashboardChoosers();

        // Configure the button bindings
        operatorInput.configureButtonBindings(driveSubsystem, ShooterSubsystem, IntakeSubsystem);

        // Add a trigger for the robot enabled
        new Trigger(() -> RobotController.isSysActive())
            .onTrue(
                new InstantCommand(() -> lightsSubsystem.setEnabled()));
    }

    private void initDashboardChoosers() {

        driveModeChooser.setDefaultOption("Tank", DriveMode.TANK);
        SmartDashboard.putData("Drive Mode", driveModeChooser);
        driveModeChooser.addOption("Dual Stick Arcade", DriveMode.DUAL_STICK_ARCADE);
        driveModeChooser.addOption("Single Stick Arcade", DriveMode.SINGLE_STICK_ARCADE);

        autoPatternChooser.setDefaultOption("Do Nothing", AutoPattern.DO_NOTHING);
        SmartDashboard.putData("Auto Pattern", autoPatternChooser);
        autoPatternChooser.addOption("Drive Forward", AutoPattern.DRIVE_FORWARD);
        autoPatternChooser.addOption("Three Note", AutoPattern.THREE_NOTE);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {

        // Pass in all of the subsystems and all of the choosers to the auto command.
        return new AutonomousCommand(
            driveSubsystem,
            autoPatternChooser);
    }
}
