package com.wkit.lost.mybatis.spring.boot.filling;

import com.wkit.lost.mybatis.filling.DefaultMetaObjectFillingHandler;
import com.wkit.lost.mybatis.filling.MetaObjectFillingDependency;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * 注册自动填充值对象
 * @author DT
 */
public class MateObjectAutoFillingRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions( AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry ) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap( importingClassMetadata.getAnnotationAttributes( EnableMetaObjectAutoFilling.class.getName() ) );
        boolean primary = Optional.ofNullable( attributes ).map( attr -> attr.getBoolean( "primary" ) ).orElse( false );
        boolean disableAutoMatching = Optional.ofNullable( attributes ).map( attr -> attr.getBoolean( "disableAutoMatching" ) ).orElse( false );
        boolean disableInsert = Optional.ofNullable( attributes ).map( attr -> attr.getBoolean( "disableInsert" ) ).orElse( false );
        boolean disableUpdate = Optional.ofNullable( attributes ).map( attr -> attr.getBoolean( "disableUpdate" ) ).orElse( false );
        boolean disableDelete = Optional.ofNullable( attributes ).map( attr -> attr.getBoolean( "disableDelete" ) ).orElse( false );
        // 创建bean定义信息
        BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition( DefaultMetaObjectFillingHandler.class );
        GenericBeanDefinition definition = ( GenericBeanDefinition ) definitionBuilder.getRawBeanDefinition();
        definition.setBeanClass( DefaultMetaObjectFillingHandler.class );
        definition.setSynthetic( true );
        definition.setAutowireCandidate( true );
        definition.setScope( SCOPE_SINGLETON );
        definition.setPrimary( primary );
        definition.setAutowireMode( AbstractBeanDefinition.AUTOWIRE_BY_TYPE );
        // 采用构造方法注入
        ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
        argumentValues.addIndexedArgumentValue( 0, disableInsert );
        argumentValues.addIndexedArgumentValue( 1, disableUpdate );
        argumentValues.addIndexedArgumentValue( 2, disableDelete );
        argumentValues.addIndexedArgumentValue( 3, disableAutoMatching );
        argumentValues.addIndexedArgumentValue( 4, getDependency() );
        definition.setConstructorArgumentValues( argumentValues );
        registry.registerBeanDefinition( "metaObjectFillingHandler", definition );
    }

    private MetaObjectFillingDependency getDependency() {
        MetaObjectFillingDependency dependency = null;
        if ( beanFactory.getBeanNamesForType( MetaObjectFillingDependency.class, false, false ).length > 0 ) {
            if ( beanFactory.containsBeanDefinition( "metaObjectFillingDependency" ) ) {
                try {
                    dependency = beanFactory.getBean( "metaObjectFillingDependency", MetaObjectFillingDependency.class );
                } catch ( Exception e ) {
                    // ignore
                }
            }
            if ( dependency == null ) {
                dependency = beanFactory.getBean( MetaObjectFillingDependency.class );
            }
        }
        return dependency;
    }

    @Override
    public void setBeanFactory( BeanFactory beanFactory ) throws BeansException {
        this.beanFactory = ( DefaultListableBeanFactory ) beanFactory;
    }
}
