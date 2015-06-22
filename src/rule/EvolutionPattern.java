package rule;

import entity.Evoluteble;

/**
 * Created by hyx on 2015/6/7.
 */
public enum EvolutionPattern {
    /**
     * ��ѡ��Ǩ��λ�ã��ٽ��в��Ը��� ��
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
     * ���в��Ը��£���ѡ��Ǩ��λ�� ��
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

    /**Эͬ�ݻ�
     * @return �����ݻ��ı���Եĸ�����Ŀ*/
    abstract public int evolute(Evoluteble world, float pi, float qi, LearningPattern learningPattern,
                                  MigrationPattern migrationPattern);


}
