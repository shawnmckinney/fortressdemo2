/*
 * This is free and unencumbered software released into the public domain.
 */
package com.mycompany;


import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.SizeUnit;
import com.inmethod.grid.column.PropertyColumn;
import com.inmethod.grid.treegrid.TreeGrid;
import com.mycompany.dao.Page3DaoMgr;
import com.mycompany.dao.Page3EO;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.openldap.fortress.util.attr.VUtil;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Example Page class for fortressdemo2 Wicket sample project.  It contains security control functions to demonstrate ANSI RBAC security concepts.
 *
 * @author Shawn McKinney
 * @version $Rev$
 */
public class Page3 extends MyBasePage
{
    private static final Logger LOG = Logger.getLogger( Page3.class.getName() );
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode node;
    private TreeGrid<DefaultTreeModel, DefaultMutableTreeNode, String> grid;
    private DefaultMutableTreeNode rootNode;
    private Form editForm;
    private Page3DaoMgr p3manager = new Page3DaoMgr();

    public Page3()
    {
        Page3ListModel page3ListModel = new Page3ListModel( new Page3EO( ), this );
        setDefaultModel(page3ListModel);
        this.editForm = new Page3Form( "pageForm", new CompoundPropertyModel<Page3EO>( new Page3EO() ) );
        this.editForm.setOutputMarkupId( true );
        add( this.editForm );
        setChildPage( ChildPage.PAGE3 );
    }

    public class Page3Form extends Form
    {
        private TextField customer;

        public Page3Form( String id, final IModel<Page3EO> model )
        {
            super( id, model );
            addDetailFields();
            addGrid();
            add( new Label( "label1", "If you see this page, ROLE_TEST_SUPER or ROLE_TEST3 is activated within your session" ) );
            addButtons();
        }

        /**
         * Add the Page Buttons for CRUD
         */
        private void addButtons()
        {
            final String szBtn1 = GlobalUtils.BTN_PAGE_3 + "." + GlobalUtils.ADD;
            add( new SecureIndicatingAjaxButton( this, szBtn1, GlobalUtils.PAGE3_OBJNAME, GlobalUtils.ADD )
            {
                @Override
                protected void onSubmit( AjaxRequestTarget target, Form form )
                {
                    Page3EO page3EO = ( Page3EO ) editForm.getModel().getObject();
                    info( szBtn1 );
                    if( page3EO != null && checkAccess() )
                    {
                        p3manager.addPage3( page3EO, this );
                        SaveModelEvent.send( getPage(), this, page3EO, target, SaveModelEvent.Operations.ADD );
                        //target.appendJavaScript(";alert('" + szBtn1 + "');");
                    }
                    else
                    {
                        target.appendJavaScript( ";alert('Unauthorized');" );
                    }
                }

                @Override
                public void onError( AjaxRequestTarget target, Form form )
                {
                    LOG.error( "submit failed: " + szBtn1 );
                }

                @Override
                protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
                {
                    super.updateAjaxAttributes( attributes );
                    AjaxCallListener ajaxCallListener = new AjaxCallListener()
                    {
                        @Override
                        public CharSequence getFailureHandler( Component component )
                        {
                            return GlobalUtils.WINDOW_LOCATION_REPLACE_DEMO_HOME_HTML;
                        }
                    };
                    attributes.getAjaxCallListeners().add( ajaxCallListener );
                }
            } );
            final String szBtn2 = GlobalUtils.BTN_PAGE_3 + "." + GlobalUtils.UPDATE;
            add( new SecureIndicatingAjaxButton( this, szBtn2, GlobalUtils.PAGE3_OBJNAME, GlobalUtils.UPDATE )
            {
                @Override
                protected void onSubmit( AjaxRequestTarget target, Form form )
                {
                    Page3EO page3EO = ( Page3EO ) editForm.getModel().getObject();
                    info( szBtn2 );
                    if( page3EO != null && checkAccess( page3EO.getCustomer() ) )
                    {
                        p3manager.updatePage3( page3EO, this );
                        SaveModelEvent.send( getPage(), this, page3EO, target, SaveModelEvent.Operations.UPDATE );
                        //target.appendJavaScript(";alert('" + szBtn2 + "');");
                    }
                    else
                    {
                        target.appendJavaScript( ";alert('Unauthorized');" );
                    }
                }

                @Override
                public void onError( AjaxRequestTarget target, Form form )
                {
                    LOG.error( "submit failed: " + szBtn2 );
                }

                @Override
                protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
                {
                    super.updateAjaxAttributes( attributes );
                    AjaxCallListener ajaxCallListener = new AjaxCallListener()
                    {
                        @Override
                        public CharSequence getFailureHandler( Component component )
                        {
                            return GlobalUtils.WINDOW_LOCATION_REPLACE_DEMO_HOME_HTML;
                        }
                    };
                    attributes.getAjaxCallListeners().add( ajaxCallListener );
                }
            } );
            final String szBtn3 = GlobalUtils.BTN_PAGE_3 + "." + GlobalUtils.DELETE;
            add( new SecureIndicatingAjaxButton( this, szBtn3, GlobalUtils.PAGE3_OBJNAME, GlobalUtils.DELETE )
            {
                @Override
                protected void onSubmit( AjaxRequestTarget target, Form form )
                {
                    Page3EO page3EO = ( Page3EO ) editForm.getModel().getObject();
                    info( szBtn3 );
                    if( page3EO != null && checkAccess( page3EO.getCustomer() ) )
                    {
                        p3manager.deletePage3ById( page3EO, this );
                        SaveModelEvent.send( getPage(), this, clearDetailFields( ), target, SaveModelEvent.Operations.DELETE );
                        target.appendJavaScript(";alert('" + szBtn3 + "');");
                    }
                    else
                    {
                        target.appendJavaScript( ";alert('Unauthorized');" );
                    }
                }

                @Override
                public void onError( AjaxRequestTarget target, Form form )
                {
                    LOG.error( "submit failed: " + szBtn3 );
                }

                @Override
                protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
                {
                    super.updateAjaxAttributes( attributes );
                    AjaxCallListener ajaxCallListener = new AjaxCallListener()
                    {
                        @Override
                        public CharSequence getFailureHandler( Component component )
                        {
                            return GlobalUtils.WINDOW_LOCATION_REPLACE_DEMO_HOME_HTML;
                        }
                    };
                    attributes.getAjaxCallListeners().add( ajaxCallListener );
                }
            } );

            final String szBtn4 = GlobalUtils.BTN_PAGE_3 + "." + GlobalUtils.SEARCH;
            add( new SecureIndicatingAjaxButton( this, szBtn4, GlobalUtils.PAGE3_OBJNAME, GlobalUtils.SEARCH )
            {
                @Override
                protected void onSubmit( AjaxRequestTarget target, Form form )
                {
                    Page3EO page3EO = ( Page3EO ) editForm.getModel().getObject();
                    if( page3EO != null && checkAccess( page3EO.getCustomer() ) )
                    {
                        this.getPage().setDefaultModel( new Page3ListModel<Page3EO>( page3EO, this.getPage() ) );
                        treeModel.reload();
                        rootNode.removeAllChildren();
                        List<Page3EO> page3EOs = ( List<Page3EO> ) this.getPage().getDefaultModelObject();
                        if ( VUtil.isNotNullOrEmpty( page3EOs ) )
                        {
                            for ( Page3EO entity : page3EOs )
                                rootNode.add( new DefaultMutableTreeNode( entity ) );
                            info( "Search returned " + page3EOs.size() + " matching objects" );
                        }
                        else
                        {
                            info( "No matching objects found" );
                        }
                    }
                    else
                    {
                        target.appendJavaScript( ";alert('Unauthorized');" );
                    }
                }

                @Override
                public void onError( AjaxRequestTarget target, Form form )
                {
                    LOG.error( "submit failed: " + szBtn4 );
                }

                @Override
                protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
                {
                    super.updateAjaxAttributes( attributes );
                    AjaxCallListener ajaxCallListener = new AjaxCallListener()
                    {
                        @Override
                        public CharSequence getFailureHandler( Component component )
                        {
                            return GlobalUtils.WINDOW_LOCATION_REPLACE_DEMO_HOME_HTML;
                        }
                    };
                    attributes.getAjaxCallListeners().add( ajaxCallListener );
                }
            } );
        }

        private void addDetailFields()
        {
            Label id = new Label( "id" );
            add( id );
            customer = new TextField( "customer" );
            add( customer );
            customer.setRequired( true );

            TextField attr_g = new TextField( "attr_g" );
            add( attr_g );
            attr_g.setRequired( false );

            TextField attr_h = new TextField( "attr_h" );
            add( attr_h );
            attr_h.setRequired( false );

            TextField attr_i = new TextField( "attr_i" );
            add( attr_i );
            attr_i.setRequired( false );
        }

        private Page3EO clearDetailFields( )
        {
            Page3EO page3EO = new Page3EO();
            setModelObject( page3EO );
            modelChanged();
            return page3EO;
        }

        @Override
        public void onEvent( final IEvent<?> event )
        {
            if ( event.getPayload() instanceof SelectModelEvent )
            {
                SelectModelEvent modelEvent = ( SelectModelEvent ) event.getPayload();
                final Page3EO page3EO = ( Page3EO ) modelEvent.getEntity();
                this.setModelObject(page3EO);
                LOG.info("Received SelectModelEvent, customer: " + page3EO.getCustomer());
            }
            else if ( event.getPayload() instanceof AjaxRequestTarget )
            {
                AjaxRequestTarget target = ( ( AjaxRequestTarget ) event.getPayload() );
                LOG.info( ".onEvent AjaxRequestTarget: " + target.toString() );
                target.add( editForm );
            }
        }
    }

    /**
     *
     */
    private void addGrid()
    {
        List<IGridColumn<DefaultTreeModel, DefaultMutableTreeNode, String>> columns =
            new ArrayList<>();

        PropertyColumn id = new PropertyColumn<DefaultTreeModel, DefaultMutableTreeNode, String, String>(
            Model.of( "ID" ), "userObject.Id");
        id.setInitialSize( 50 );
        columns.add(id);

        PropertyColumn customer = new PropertyColumn<DefaultTreeModel, DefaultMutableTreeNode, String, String>(
            Model.of( "Customer" ), "userObject.Customer");
        customer.setInitialSize( 200 );
        columns.add(customer);

        PropertyColumn attrG = new PropertyColumn<DefaultTreeModel, DefaultMutableTreeNode, String, String>(
            Model.of("Attribute G"), "userObject.Attr_g");
        attrG.setInitialSize( 200 );
        columns.add(attrG);

        PropertyColumn attrH = new PropertyColumn<DefaultTreeModel, DefaultMutableTreeNode, String, String>(
            Model.of("Attribute H"), "userObject.Attr_h");
        attrH.setInitialSize( 200 );
        columns.add(attrH);

        PropertyColumn attrI = new PropertyColumn<DefaultTreeModel, DefaultMutableTreeNode, String, String>(
            Model.of("Attribute I"), "userObject.Attr_i");
        attrI.setInitialSize( 200 );
        columns.add(attrI);

        List<Page3EO> page3EOs = (List<Page3EO>) getDefaultModel().getObject();
        treeModel = createTreeModel(page3EOs);
        grid = new TreeGrid<DefaultTreeModel, DefaultMutableTreeNode, String>("page3treegrid", treeModel, columns)
        {
            @Override
            public void selectItem(IModel itemModel, boolean selected)
            {
                node = (DefaultMutableTreeNode) itemModel.getObject();
                if(!node.isRoot())
                {
                    Page3EO page3EO = (Page3EO) node.getUserObject();
                    LOG.debug( "TreeGrid.addGrid.selectItem selected customer =" + page3EO.getCustomer() );
                    if (super.isItemSelected(itemModel))
                    {
                        LOG.debug("TreeGrid.addGrid.selectItem item is selected");
                        super.selectItem(itemModel, false);
                    }
                    else
                    {
                        super.selectItem(itemModel, true);
                        LOG.info( "Page3 List Entity Customer: " + page3EO.getCustomer() );
                        SelectModelEvent.send(getPage(), this, page3EO);
                    }
                }
            }
        };
        grid.setContentHeight(10, SizeUnit.EM);
        grid.setAllowSelectMultiple(false);
        grid.setClickRowToSelect(true);
        grid.setClickRowToDeselect(false);
        grid.setSelectToEdit(false);
        // expand the root node
        grid.getTreeState().expandNode((TreeNode ) treeModel.getRoot());
        this.add(grid);
        grid.setOutputMarkupId(true);
    }

    @Override
    public void onEvent(IEvent event)
    {
        // Page level SaveModelEvents triggered by button in the Page Detail Form:
        if (event.getPayload() instanceof SaveModelEvent)
        {
            SaveModelEvent modelEvent = (SaveModelEvent) event.getPayload();
            switch (modelEvent.getOperation())
            {
                case ADD:
                    add((Page3EO)modelEvent.getEntity());
                    break;
                case UPDATE:
                    modelChanged();
                    break;
                case DELETE:
                    prune();
                    break;
                default:
                    LOG.error( "onEvent caught invalid operation" );
                    break;
            }
            AjaxRequestTarget target = ((SaveModelEvent) event.getPayload()).getAjaxRequestTarget();
            LOG.debug(".onEvent AJAX - Page3 - SaveModelEvent: " + target.toString());
        }
        // Page level AJAX events - replace TreeGrid:
        else if ( event.getPayload() instanceof AjaxRequestTarget )
        {
            LOG.info("Page level AjaxRequestTarget Event Occurred");
            AjaxRequestTarget target = ( ( AjaxRequestTarget ) event.getPayload() );
            target.add( grid );
        }
    }

    /**
     *
     * @param page3EOs
     * @return
     */
    private DefaultTreeModel createTreeModel(List<Page3EO> page3EOs)
    {
        DefaultTreeModel model;
        Page3EO rootObject = new Page3EO(  );
        rootNode = new DefaultMutableTreeNode(rootObject);
        model = new DefaultTreeModel(rootNode);
        if (page3EOs == null)
            LOG.debug("no Page3 datasets found");
        else
        {
            LOG.debug( ".createTreeModel Groups found:" + page3EOs.size() );
            for (Page3EO page3EO : page3EOs)
                rootNode.add(new DefaultMutableTreeNode(page3EO));
        }
        return model;
    }

    private void prune()
    {
        removeSelectedItems(grid);
    }

    private void add(Page3EO entity)
    {
        if (getDefaultModelObject() != null)
        {
            treeModel.insertNodeInto(new DefaultMutableTreeNode(entity), rootNode, 0);
        }
    }

    private void removeSelectedItems(TreeGrid<DefaultTreeModel, DefaultMutableTreeNode, String> grid)
    {
        Collection<IModel<DefaultMutableTreeNode>> selected = grid.getSelectedItems();
        for (IModel<DefaultMutableTreeNode> model : selected)
        {
            DefaultMutableTreeNode node = model.getObject();
            treeModel.removeNodeFromParent(node);
            Page3EO page3EO = (Page3EO) node.getUserObject();
            LOG.debug(".removeSelectedItems page3 node: " + page3EO.getCustomer());
        }
    }
}
