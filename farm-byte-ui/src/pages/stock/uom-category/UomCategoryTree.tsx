import {ColumnDef} from "@tanstack/react-table";
import {UomCategoryModel} from "@/pages/stock/uom-category/UomCategoryModel.ts";
import {useSelector, useDispatch} from "react-redux";
import {
    getLoading,
    getPageSize,
    getTotalElements, getTotalPages, searchUomCategories,
    selectUomCategories
} from "@/pages/stock/uom-category/UomCategorySlice.ts";
import {useEffect, useState} from "react";
import {DEFAULT_DIRECTION_VALUE, DEFAULT_PAGE_VALUE} from "@/constants/page.constant.ts";
import {Direction} from "@/constants/directionConstant.ts";
import DataTable from "@/components/DataTable.tsx";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import {RootDispatch} from "@/Store.ts";
import useDebounce from "@/hooks/useDebounce.tsx";
import {DEBOUNCE_TIMEOUT} from "@/constants/app.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {Checkbox} from "@/components/ui/checkbox.tsx";


const UomCategoryTree = () => {

    const uomCategories: Array<UomCategoryModel> = useSelector(selectUomCategories);
    const pageSize: number = useSelector(getPageSize);
    const totalElements = useSelector(getTotalElements);
    const totalPages = useSelector(getTotalPages);
    const isLoading = useSelector(getLoading);
    const navigate = useNavigate();

    const [page, setPage] = useState<number>(DEFAULT_PAGE_VALUE);
    const [size, setSize] = useState<number>(pageSize);
    const [keyword, setKeyword] = useState<string>('');
    const debounceKeyword = useDebounce(keyword, DEBOUNCE_TIMEOUT );
    const dispatch = useDispatch<RootDispatch>();

    const [, setSearchParam] = useState<SearchParamModel>({
        page: DEFAULT_PAGE_VALUE,
        size: pageSize,
        attribute: "name",
        direction: Direction.ASC,
        keyword: keyword
    });

    const {t} = useTranslation(['home']);

    const columns: ColumnDef<UomCategoryModel>[] = [
        {
            id: "select",
            header: ({ table }) => (
                <Checkbox
                    checked={
                        table.getIsAllPageRowsSelected() ||
                        (table.getIsSomePageRowsSelected() && "indeterminate")
                    }
                    onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
                    aria-label="Select all"
                />
            ),
            cell: ({ row }) => (
                <Checkbox
                    checked={row.getIsSelected()}
                    onCheckedChange={(value) => row.toggleSelected(!!value)}
                    aria-label="Select row"
                />
            ),
            enableSorting: false,
            enableHiding: false
        },
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
                    <span  onClick={() => handleEdit(row.original)} className="material-symbols-outlined text-destructive text-xl cursor-pointer">delete</span>
                </div>
            )
        }
    ];


    useEffect(() => {
        dispatch(searchUomCategories({
            page: page,
            size: size,
            attribute: "name",
            direction: DEFAULT_DIRECTION_VALUE,
            keyword: keyword
        }));
    }, [debounceKeyword, page, size]);

    const handlePageChange = (page: number) => {
        setSearchParam({page: page, size: size, attribute: "name", direction: DEFAULT_DIRECTION_VALUE, keyword: keyword});
        //TODO add page in pageInfo in backend and manage page in store
        setPage(page);
    }

    const handleSizeChange = (size: number) => {
        setSearchParam({page: DEFAULT_PAGE_VALUE, size: size, attribute: "name", direction: DEFAULT_DIRECTION_VALUE, keyword: keyword});
        //TODO add page in pageInfo in backend and manage page in store
        setSize(size);
        setPage(DEFAULT_PAGE_VALUE);
    }

    const handleFilterChange = (keyword: string) => {
        setKeyword(keyword);
        setPage(DEFAULT_PAGE_VALUE);
    }

    const handleDelete = (rows: Array<UomCategoryModel>) => {
        console.log(rows); //TODO
    }

    const handleEdit = (row: UomCategoryModel) => {
        navigate(`/stock/uom-categories/details/${row.id}`);
    }

    const handleNew = () => {
        navigate('/stock/uom-categories/new');
    }

    const handleClear = () => {
        setKeyword('');
    }

    return (
        <div className="text-3xl text-primary min-h-full">
            <DataTable
                title={'uom_category_tree_title'}
                columns={columns} data={uomCategories}
                totalElements={totalElements}
                page={page}
                size={size}
                totalPages={totalPages}
                isLoading={isLoading}
                keyword={keyword}
                handlePageChange={handlePageChange}
                handleNew={handleNew}
                handleFilterChange={handleFilterChange}
                handleSizeChange={handleSizeChange}
                handleClear={handleClear}
                handleDelete={handleDelete}
            />

        </div>
    );
}

export default UomCategoryTree;