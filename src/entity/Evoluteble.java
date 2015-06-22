package entity;

import rule.LearningPattern;
import rule.MigrationPattern;

/**
 * Created by hyx on 2015/6/7.
 */
public interface Evoluteble {
    void learning(float pi, LearningPattern learningPattern);

    void migrate(float qi, MigrationPattern migrationPattern);

    int updateIndividualStrategy();

    void updateIndividualInteractionIntensity();
}
