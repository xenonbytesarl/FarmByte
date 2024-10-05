import {ColumnDef} from "@tanstack/react-table";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {useSelector} from "react-redux";
import {
    findUomCategories,
    getLoading,
    getPageSize,
    getTotalElements, getTotalPages, searchUomCategories,
    selectUomCategories
} from "@/pages/inventory/uom-category/UomCategorySlice.ts";
import {useCallback, useEffect, useState} from "react";
import {DEFAULT_PAGE_VALUE} from "@/constants/page.constant.ts";
import {Direction} from "@/constants/directionConstant.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import DataTable from "@/components/DataTable.tsx";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {store} from "@/Store.ts";
import useDebounce from "@/hooks/useDebounce.tsx";
import {DEBOUNCE_TIMEOUT} from "@/constants/app.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";


const UomCategoryTree = () => {

    const uomCategories: Array<UomCategoryModel> = useSelector(selectUomCategories);
    const size: number = useSelector(getPageSize);
    const totalElements = useSelector(getTotalElements);
    const totalPages = useSelector(getTotalPages);
    const isLoading = useSelector(getLoading);
    const navigate = useNavigate();

    const [page, setPage] = useState<number>(DEFAULT_PAGE_VALUE);
    const [keyword, setKeyword] = useState<string>('');
    const debounceKeyword = useDebounce(keyword, DEBOUNCE_TIMEOUT );

    const [findParam, setFindParam] = useState<FindParamModel>({
        page: DEFAULT_PAGE_VALUE,
        size: size,
        attribute: "name",
        direction: Direction.ASC
    });

    const {t} = useTranslation(['home']);

    const columns: ColumnDef<UomCategoryModel>[] = [
        {
            accessorKey: "name",
            header: () => (<div className="text-left">{t("uom_category_tree_name")}</div>),
        },
        {
            accessorKey: "action",
            header: () => "",
            cell: ({row}) => (
                <div  className="flex flex-row justify-end items-center gap-4 text-end ">
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-primary text-xl cursor-pointer">edit</span>
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-red-500 text-xl cursor-pointer">delete</span>
                </div>
            )
        }
    ];

    useEffect(() => {
        store.dispatch(findUomCategories(findParam));
    }, [findParam]);

    useEffect(() => {

            store.dispatch(searchUomCategories({
                page: page,
                size: size,
                attribute: "name",
                direction: Direction.ASC,
                keyword: keyword
            }));

    }, [debounceKeyword]);

    const handlePaginatorChange = (page: number, size: number) => {
        setFindParam({page: page, size: size, attribute: "name", direction: Direction.ASC});
        //TODO add page in pageInfo in backend and manage page in store
        setPage(page);
    }

    const handleEdit = (row: UomCategoryModel) => {
        navigate(`/inventory/uom-categories/detail/${row.id}`);
    }

    const handleNew = (link: string) => {
        navigate(link);
    }

    const handleFilter = (keyword: string) => {
        setKeyword(keyword);
    }

    const handleClear = () => {
        setKeyword('');
    }

    return (
        <div className="text-3xl text-amber-700 min-h-full">
            <DataTable
                title={'uom_category_form_new_title'}
                columns={columns} data={uomCategories}
                totalElements={totalElements}
                page={page}
                size={size}
                totalPages={totalPages}
                isLoading={isLoading}
                newLink={'/inventory/uom-categories/new'}
                keyword={keyword}
                handlePaginatorChange={handlePaginatorChange}
                handleNew={handleNew}
                handleFilter={handleFilter}
                handleClear={handleClear}
            />

        </div>
    );
}

export default UomCategoryTree;