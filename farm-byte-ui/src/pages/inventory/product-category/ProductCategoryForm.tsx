import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {useToast} from "@/hooks/use-toast.ts";
import {ProductCategoryModel} from "@/pages/inventory/product-category/ProductCategoryModel.ts";
import {useSelector} from "react-redux";
import {RootState, store} from "@/Store.ts";
import {
    findProductCategoryById,
    getLoading,
    selectProductCategoryById,
    createProductCategory, updateProductCategory
} from "@/pages/inventory/product-category/ProductCategorySlice.ts";
import {useEffect, useState} from "react";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {ToastType} from "@/shared/constant/globalConstant.ts";
import {cn} from "@/lib/utils.ts";
import {unwrapResult} from "@reduxjs/toolkit";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Label} from "@/components/ui/label.tsx";

const ProductCategoryForm = () => {

    const {t} = useTranslation(['home']);

    const {productCategoryId} = useParams();

    const {toast} = useToast();

    // @ts-ignore
    const productCategory: ProductCategoryModel = useSelector((state:RootState) => selectProductCategoryById(state, productCategoryId));
    const isLoading: boolean = useSelector(getLoading);

    const navigate = useNavigate();
    const [mode, setMode] = useState<FormModeType>(productCategoryId? FormModeType.READ: FormModeType.CREATE);

    const ProductCategorySchema = Zod.object({
        name: Zod.string().min(1, t('product_category_form_name_required_message')),
    });
    const defaultValuesProductCategory: ProductCategoryModel = {
        id: "",
        name: "",
        parentProductCategoryId: "",
        active: true
    };
    const { register, handleSubmit, formState: {errors, isValid }, reset, getValues } = useForm<ProductCategoryModel>({
        defaultValues: defaultValuesProductCategory,
        resolver: zodResolver(ProductCategorySchema),
        mode: "onTouched",
    });

    useEffect(() => {
        if(!productCategory && productCategoryId) {
            console.log(productCategoryId);
            store.dispatch(findProductCategoryById(productCategoryId));
        }
    }, [store.dispatch, productCategory]);

    useEffect(() => {
        if(productCategory) {
            reset(productCategory)
        }
    }, [productCategory, reset]);

    const showToast = (variant: ToastType, message: string) => {
        toast({
            className: cn(
                'top-0 right-0 flex fixed md:max-w-[420px] md:top-4 md:right-4'
            ),
            variant: variant,
            title: "FarmByte",
            description: t(message),
        });
    }

    const onSubmit = () => {
        const productCategoryFormValue: ProductCategoryModel = getValues();
        if (productCategoryFormValue.id) {
            store.dispatch(updateProductCategory({productCategoryId: productCategoryFormValue.id, productCategory: productCategoryFormValue}))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    showToast("info", response.message);
                })
                .catch((error) => {
                    setMode(FormModeType.EDIT);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                })
        } else {
            store.dispatch(createProductCategory(productCategoryFormValue))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    showToast("success", response.message);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    navigate(`/inventory/product-categories/details/${response.data.content.id}`);
                })
                .catch((error) => {
                    setMode(FormModeType.CREATE);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                });
        }
    }

    const onEdit = () => {
        setMode(FormModeType.EDIT);
    }

    const onCancel = () => {
        if(productCategory) {
            reset(productCategory);
            setMode(FormModeType.READ);
        } else {
            reset();
        }
    }

    const onCreate = () => {
        navigate('/inventory/product-categories/new');
        setMode(FormModeType.CREATE);
        reset(defaultValuesProductCategory);
    }
    return (
        <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Toaster/>
            <Card className="">
                <CardHeader>
                    <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/inventory/product-categories`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                        <span
                            className="text-2xl">{t(productCategoryId ? 'product_category_form_edit_title' : 'product_category_form_new_title')}</span>
                    </CardTitle>
                    <CardDescription>
                    <span className="flex flex-row w-full m-5">
                        <FormCrudButton
                            mode={mode}
                            isLoading={isLoading}
                            isValid={isValid}
                            onEdit={onEdit}
                            onCancel={onCancel}
                            onCreate={onCreate}
                        />
                        <span className="flex flex-row justify-end items-center gap-3 w-6/12">
                        </span>
                    </span>
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="grid w-full items-center gap-4">
                        <Input id="name" type="hidden" {...register("id")} />

                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="name">{t('product_category_form_name_label')}</Label>
                            <Input id="name" type="text" {...register("name")}
                                   disabled={mode === FormModeType.READ || isLoading}/>
                            <small className="text-red-500">{errors.name?.message}</small>
                        </div>
                    </div>
                </CardContent>
                <CardFooter className="flex justify-between py-5">
                </CardFooter>
            </Card>
        </form>
    );
};

export default ProductCategoryForm;