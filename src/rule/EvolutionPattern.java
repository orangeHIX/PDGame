package rule;

import entity.Evoluteble;

/**
 * Created by hyx on 2015/6/7.
 */
public enum EvolutionPattern {
    /**
     * 先选好迁徙位置，再进行策略更新 ？
     */
    CDO {
        @Override
        public int evolute(Evoluteble world,
                             float pi, float qi,
                             LearningPattern learningPattern, MigrationPattern migrationPattern) {
            int change = 0;
            world.learning(pi, learningPattern);
            world.migrate(qi, migrationPattern);
            change = world.updateIndividualStrategy();

            world.updateIndividualInteractionIntensity();
            return change;
        }
    },
    /**
     * 进行策略更新，再选择迁徙位置 ？
     */
    COD {
        @Override
        public int evolute(Evoluteble world, float pi, float qi, LearningPattern learningPattern, MigrationPattern migrationPattern) {
            int change = 0;
            world.learning(pi, learningPattern);
            change = world.updateIndividualStrategy();
            world.migrate(qi, migrationPattern);

            world.updateIndividualInteractionIntensity();
            return change;
        }
    };

    /**协同演化
     * @return 本次演化改变策略的个体数目*/
    abstract public int evolute(Evoluteble world, float pi, float qi, LearningPattern learningPattern,
                                  MigrationPattern migrationPattern);


}
