import {Link} from "react-router-dom";

const ProductCategoryTree = () => {
    return (
        <div className="text-3xl text-amber-700">
            PRODUCT CATEGORY TREE<br/>
            <Link to="/inventory/product-categories/new">New Product Category</Link><br/>
            <Link to="/inventory/product-categories/detail/jdkcdcjbc-65645-rvrj65-vvrjkkjb">Detail Product Category</Link>
            <br/>
        </div>
    );
}

export default ProductCategoryTree;